package com.borred.zimran_test_app.repositories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.borred.ktor_client.network.search.repos.SearchRepositoryApi
import com.borred.ktor_client.network.search.repos.model.GitRepository
import com.borred.ktor_client.network.search.repos.model.ReposSort
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import kotlin.time.Duration.Companion.milliseconds

@HiltViewModel
class SearchRepositoriesViewModel
@Inject constructor(
    private val repositoryApi: SearchRepositoryApi
) : ViewModel() {

    private val _searchTextFlow = MutableStateFlow("")
    val searchTextFlow: StateFlow<String> = _searchTextFlow.asStateFlow()

    private val _sortByFlow = MutableStateFlow<ReposSort>(ReposSort.STARS)
    val sortByFlow: StateFlow<ReposSort> = _sortByFlow.asStateFlow()

    @OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
    val gitReposPaging: Flow<PagingData<GitRepository>> = combine(
        _searchTextFlow
            .map { it.trim() }
            .debounce(200.milliseconds),
        _sortByFlow
    ) { text, sort ->
        text to sort
    }.flatMapLatest { (text, sort) ->
        if (text.isEmpty()) {
            return@flatMapLatest emptyFlow()
        }
        getPagingFlow(text, sort)
    }.cachedIn(viewModelScope)

    private val pagingConfig = PagingConfig(
        pageSize = 30,
        initialLoadSize = 30
    )

    fun setSearchText(text: String) {
        _searchTextFlow.value = text
    }

    fun setSortBy(sort: ReposSort) {
        _sortByFlow.value = sort
    }

    private fun getPagingFlow(
        text: String,
        sort: ReposSort
    ): Flow<PagingData<GitRepository>> {
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
    }
}
