package com.borred.zimran_test_app.users

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.borred.ktor_client.network.search.users.SearchUsersApi
import com.borred.ktor_client.network.search.users.model.GitUser
import com.borred.ktor_client.network.search.users.model.UsersSort
import com.borred.zimran_test_app.users.model.GitUserUI
import com.borred.zimran_test_app.users.model.toUI

class GitUserPagingSource(
    private val text: String,
    private val sort: UsersSort,
    private val usersApi: SearchUsersApi
) : PagingSource<Int, GitUserUI>() {

    override fun getRefreshKey(state: PagingState<Int, GitUserUI>): Int? {
        return state.anchorPosition?.let {
            state.closestPageToPosition(it)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(it)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, GitUserUI> {
        val page = params.key ?: 1
        usersApi
        usersApi.searchUsers(
            name = text,
            sort = sort,
            page = page,
            perPage = params.loadSize
        ).onFailure {
            return LoadResult.Error(it)
        }.onSuccess { response ->
            return LoadResult.Page(
                data = response.items.map { it.toUI() },
                prevKey = if (page == 1) {
                    null
                } else {
                    page - 1
                },
                nextKey = if (response.items.size < params.loadSize) {
                    null
                } else {
                    page + 1
                }
            )
        }
        return LoadResult.Invalid()
    }
}
