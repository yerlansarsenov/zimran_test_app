package com.borred.ktor_client.network.search.users

import com.borred.ktor_client.local.auth.AccessTokenDataStore
import com.borred.ktor_client.network.SEARCH_CLIENT
import com.borred.ktor_client.network.search.setToken
import com.borred.ktor_client.network.search.users.model.SearchUsersResponse
import com.borred.ktor_client.network.search.users.model.UsersSort
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import javax.inject.Inject
import javax.inject.Named

interface SearchUsersApi {

    suspend fun searchUsers(
        name: String,
        sort: UsersSort,
        page: Int,
        perPage: Int = 30
    ): Result<SearchUsersResponse>
}

internal class SearchUsersApiImpl
@Inject
constructor(
    @Named(SEARCH_CLIENT)
    private val httpClient: HttpClient,
    private val tokenDataStore: AccessTokenDataStore,
) : SearchUsersApi {

    override suspend fun searchUsers(
        name: String,
        sort: UsersSort,
        page: Int,
        perPage: Int
    ): Result<SearchUsersResponse> {
        return kotlin.runCatching {
            httpClient.get(
                urlString = "search/users"
            ) {
                setToken(tokenDataStore = tokenDataStore)
                parameter("q", "$name in:name")
                parameter("page", page)
                parameter("per_page", perPage)
                parameter("sort", sort.value)
            }.body()
        }
    }
}
