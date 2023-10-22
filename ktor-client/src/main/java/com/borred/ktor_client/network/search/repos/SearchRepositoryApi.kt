package com.borred.ktor_client.network.search.repos

import com.borred.ktor_client.local.auth.AccessTokenDataStore
import com.borred.ktor_client.network.SEARCH_CLIENT
import com.borred.ktor_client.network.search.repos.model.ReposSort
import com.borred.ktor_client.network.search.repos.model.SearchReposResponse
import com.borred.ktor_client.network.search.repos.model.SearchReposResponseDTO
import com.borred.ktor_client.network.search.setToken
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.int
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import javax.inject.Inject
import javax.inject.Named

interface SearchRepositoryApi {

    suspend fun searchRepos(
        name: String,
        sort: ReposSort,
        page: Int, // start with 1
        perPage: Int = 30
    ) : Result<SearchReposResponse>
}

private const val REQUIRES_AUTH_CODE = 401

internal class SearchRepositoryApiImpl
@Inject
constructor(
    @Named(SEARCH_CLIENT)
    private val httpClient: HttpClient,
    private val tokenDataStore: AccessTokenDataStore,
) : SearchRepositoryApi {

    override suspend fun searchRepos(
        name: String,
        sort: ReposSort,
        page: Int,
        perPage: Int
    ): Result<SearchReposResponse> {
        return kotlin.runCatching {
            return@runCatching httpClient.get(
                urlString = "search/repositories"
            ) {
                setToken(tokenDataStore = tokenDataStore)
                parameter("q", "$name in:name")
                parameter("page", page)
                parameter("per_page", perPage)
                parameter("sort", sort.value)
            }.body<SearchReposResponseDTO>().toDomain()
        }
    }

    private fun getLanguages(): Map<String, Int> {
        val languagesJson = Json.parseToJsonElement(
            """
                {
                  "Assembly": 53960,
                  "C": 8308
                }
            """.trimIndent()
        )
        return languagesJson.jsonObject.map {
            val language = it.key
            val count = it.value.jsonPrimitive.int
            language to count
        }.toMap()
    }
}
