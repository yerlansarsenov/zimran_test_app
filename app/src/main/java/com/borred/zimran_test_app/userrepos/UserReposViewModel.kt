package com.borred.zimran_test_app.userrepos

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.borred.ktor_client.network.search.repos.SearchRepositoryApi
import com.borred.ktor_client.network.search.repos.model.GitRepository
import com.borred.ktor_client.network.search.repos.model.ReposSort
import com.borred.ktor_client.network.search.users.model.GitUser
import com.borred.zimran_test_app.safeLaunch
import com.borred.zimran_test_app.ui.SpecialCharsMap
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.serialization.json.Json
import javax.inject.Inject

@HiltViewModel
class UserReposViewModel @Inject constructor(
    private val repositoryApi: SearchRepositoryApi,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    val user = savedStateHandle.get<String>("user")?.let {
        Log.e("HERE!!", "UserReposViewModel: user = $it")
        var str = it
        SpecialCharsMap.forEach { (value, key) ->
            str = str.replace(value, key)
        }
        val _object = Json.decodeFromString<GitUser>(str)
        Log.e("HERE!!", "UserReposViewModel: _object = $_object")
        _object
    }
    private val login = user?.login ?: "BjarneStroustrup"

    private val _sortByFlow = MutableStateFlow<ReposSort>(ReposSort.STARS)
    val sortByFlow: StateFlow<ReposSort> = _sortByFlow.asStateFlow()

    private var searchingJob: Job? = null

    private val pagingConfig = PagingConfig(
        pageSize = 30,
        initialLoadSize = 30
    )

    private val _gitReposPaging: MutableState<Flow<PagingData<GitRepository>>> = mutableStateOf(
        getPagingFlow(login, _sortByFlow.value)
    )
    val gitReposPaging: State<Flow<PagingData<GitRepository>>> = _gitReposPaging

    fun setSortBy(sort: ReposSort) {
        _sortByFlow.value = sort
        updateParams()
    }

    private fun updateParams() {
        val sort = _sortByFlow.value
        searchingJob?.cancel()
        searchingJob = viewModelScope.safeLaunch {
            delay(200)
            _gitReposPaging.value = getPagingFlow(login, sort)
        }
    }

    private fun getPagingFlow(
        login: String,
        sort: ReposSort
    ): Flow<PagingData<GitRepository>> {
        return Pager(
            config = pagingConfig
        ) {
            UserRepositoriesPagingSource(
                login = login,
                sort = sort,
                repositoryApi = repositoryApi
            )
        }.flow
            .flowOn(Dispatchers.IO)
            .cachedIn(viewModelScope)
    }
}
