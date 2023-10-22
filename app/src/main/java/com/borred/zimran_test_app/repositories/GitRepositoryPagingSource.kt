package com.borred.zimran_test_app.repositories

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.borred.ktor_client.network.search.repos.SearchRepositoryApi
import com.borred.ktor_client.network.search.repos.model.GitRepository
import com.borred.ktor_client.network.search.repos.model.ReposSort

class GitRepositoryPagingSource(
    private val text: String,
    private val sort: ReposSort,
    private val repositoryApi: SearchRepositoryApi
) : PagingSource<Int, GitRepository>() {

    override fun getRefreshKey(state: PagingState<Int, GitRepository>): Int? {
        return state.anchorPosition?.let {
            state.closestPageToPosition(it)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(it)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, GitRepository> {
        val page = params.key ?: 1
        repositoryApi.searchRepos(
            name = text,
            sort = sort,
            page = page,
            perPage = params.loadSize
        ).onFailure {
            return LoadResult.Error(it)
        }.onSuccess { response ->
            return LoadResult.Page(
                data = response.items,
                prevKey = if (page == 1) {
                    null
                } else {
                    page - 1
                },
                nextKey = if (response.items.isEmpty()) {
                    null
                } else {
                    page + 1
                }
            )
        }
        return LoadResult.Invalid()
    }
}
