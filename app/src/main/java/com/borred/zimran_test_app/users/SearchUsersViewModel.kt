package com.borred.zimran_test_app.users

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.borred.ktor_client.network.search.users.SearchUsersApi
import com.borred.ktor_client.network.search.users.UsersHistoryDataStore
import com.borred.ktor_client.network.search.users.model.UsersSort
import com.borred.zimran_test_app.safeLaunch
import com.borred.zimran_test_app.users.model.GitUserUI
import com.borred.zimran_test_app.users.model.toDomain
import com.borred.zimran_test_app.users.model.toUI
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class SearchUsersViewModel
@Inject constructor(
    private val usersApi: SearchUsersApi,
    private val dataStore: UsersHistoryDataStore
) : ViewModel() {

    private val _searchTextFlow = MutableStateFlow("")
    val searchTextFlow: StateFlow<String> = _searchTextFlow.asStateFlow()

    private val _sortByFlow = MutableStateFlow<UsersSort>(UsersSort.FOLLOWERS)
    val sortByFlow: StateFlow<UsersSort> = _sortByFlow.asStateFlow()

    private var searchingJob: Job? = null

    private val historyStateFlow = dataStore.getSearchHistoryFlow()
        .map { list -> list.map { it.toUI(true) } }
        .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    private val _gitUsersPaging: MutableState<Flow<PagingData<GitUserUI>>> = mutableStateOf(
        emptyFlow()
    )
    val gitUsersPaging: State<Flow<PagingData<GitUserUI>>> = _gitUsersPaging

    private val pagingConfig = PagingConfig(
        pageSize = 30,
        initialLoadSize = 30
    )

    fun setSearchText(text: String) {
        _searchTextFlow.value = text
        updateParams()
    }

    fun setSortBy(sort: UsersSort) {
        _sortByFlow.value = sort
        updateParams()
    }

    private fun updateParams() {
        val text = _searchTextFlow.value
        val sort = _sortByFlow.value
        searchingJob?.cancel()
        searchingJob = viewModelScope.safeLaunch {
            delay(200)
            _gitUsersPaging.value = combine(
                getPagingFlow(text, sort),
                historyStateFlow
            ) { pager, history ->
                pager.map { repo ->
                    val isSeen = history.any { it.id == repo.id }
                    repo.copy(isSeen = isSeen)
                }
            }.flowOn(Dispatchers.Default)
        }
    }

    private fun getPagingFlow(
        text: String,
        sort: UsersSort
    ): Flow<PagingData<GitUserUI>> {
        return Pager(
            config = pagingConfig
        ) {
            GitUserPagingSource(
                text = text,
                sort = sort,
                usersApi = usersApi
            )
        }.flow
            .flowOn(Dispatchers.IO)
            .cachedIn(viewModelScope)
    }

    fun addUserToHistory(item: GitUserUI) {
        viewModelScope.safeLaunch {
            dataStore.addToHistory(item.toDomain())
        }
    }
}
