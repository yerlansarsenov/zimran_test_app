package com.borred.zimran_test_app.userrepos

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.borred.ktor_client.network.search.repos.SearchRepositoryApi
import com.borred.ktor_client.network.search.repos.model.ReposSort
import com.borred.zimran_test_app.repositories.model.GitRepositoryUI
import com.borred.zimran_test_app.repositories.model.toUI

class UserRepositoriesPagingSource(
    private val login: String,
    private val sort: ReposSort,
    private val repositoryApi: SearchRepositoryApi
) : PagingSource<Int, GitRepositoryUI>() {

    override fun getRefreshKey(state: PagingState<Int, GitRepositoryUI>): Int? {
        return state.anchorPosition?.let {
            state.closestPageToPosition(it)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(it)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, GitRepositoryUI> {
        val page = params.key ?: 1
        repositoryApi.getReposOfUserByLogin(
            login = login,
            sort = sort,
            page = page,
            perPage = params.loadSize
        ).onFailure {
            return LoadResult.Error(it)
        }.onSuccess { items ->
            return LoadResult.Page(
                data = items.map { it.toUI() },
                prevKey = if (page == 1) {
                    null
                } else {
                    page - 1
                },
                nextKey = if (items.size < params.loadSize) {
                    null
                } else {
                    page + 1
                }
            )
        }
        return LoadResult.Invalid()
    }
}
