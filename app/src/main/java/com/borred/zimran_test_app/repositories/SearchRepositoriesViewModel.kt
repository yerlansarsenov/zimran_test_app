package com.borred.zimran_test_app.repositories

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
import com.borred.ktor_client.network.search.repos.RepositoriesHistoryDataStore
import com.borred.ktor_client.network.search.repos.SearchRepositoryApi
import com.borred.ktor_client.network.search.repos.model.GitRepository
import com.borred.ktor_client.network.search.repos.model.ReposSort
import com.borred.zimran_test_app.repositories.model.GitRepositoryUI
import com.borred.zimran_test_app.repositories.model.toDomain
import com.borred.zimran_test_app.repositories.model.toUI
import com.borred.zimran_test_app.safeLaunch
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
class SearchRepositoriesViewModel
@Inject constructor(
    private val repositoryApi: SearchRepositoryApi,
    private val historyDataStore: RepositoriesHistoryDataStore
) : ViewModel() {

    private val _searchTextFlow = MutableStateFlow("")
    val searchTextFlow: StateFlow<String> = _searchTextFlow.asStateFlow()

    private val _sortByFlow = MutableStateFlow<ReposSort>(ReposSort.STARS)
    val sortByFlow: StateFlow<ReposSort> = _sortByFlow.asStateFlow()

    private var searchingJob: Job? = null

    private val historyStateFlow = historyDataStore.getSearchHistoryFlow()
        .map { list -> list.map { it.toUI(true) } }
        .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    private val _gitReposPaging: MutableState<Flow<PagingData<GitRepositoryUI>>> = mutableStateOf(emptyFlow())
    val gitReposPaging: State<Flow<PagingData<GitRepositoryUI>>> = _gitReposPaging

    private val pagingConfig = PagingConfig(
        pageSize = 30,
        initialLoadSize = 30
    )

    fun setSearchText(text: String) {
        _searchTextFlow.value = text
        updateParams()
    }

    fun setSortBy(sort: ReposSort) {
        _sortByFlow.value = sort
        updateParams()
    }

    fun addToHistory(item: GitRepositoryUI) {
        viewModelScope.safeLaunch {
            historyDataStore.addToHistory(item.toDomain())
        }
    }

    private fun updateParams() {
        val text = _searchTextFlow.value
        val sort = _sortByFlow.value
        searchingJob?.cancel()
        searchingJob = viewModelScope.safeLaunch {
            delay(200)
            _gitReposPaging.value = combine(
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
        sort: ReposSort
    ): Flow<PagingData<GitRepositoryUI>> {
        return Pager(
            config = pagingConfig
        ) {
            GitRepositoryPagingSource(
                text = text,
                sort = sort,
                repositoryApi = repositoryApi
            )
        }.flow
            .flowOn(Dispatchers.IO)
            .cachedIn(viewModelScope)
    }
}
