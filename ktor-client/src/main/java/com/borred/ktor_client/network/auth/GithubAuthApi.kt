package com.borred.ktor_client.network.auth

import com.borred.ktor_client.network.AUTH_CLIENT
import com.borred.ktor_client.network.auth.model.AccessToken
import com.borred.ktor_client.network.auth.model.GetAccessTokenRequestBody
import com.borred.ktor_client.network.client_secret.CLIENT_SECRET
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import javax.inject.Inject
import javax.inject.Named

interface GithubAuthApi {

    suspend fun getAccessToken(
        code: String
    ) : Result<AccessToken>
}

internal class GithubAuthApiImpl
@Inject
constructor(
    @Named(AUTH_CLIENT)
    private val httpClient: HttpClient,
) : GithubAuthApi {

    override suspend fun getAccessToken(code: String): Result<AccessToken> {
        return kotlin.runCatching {
            val request = httpClient.post(
                urlString = "login/oauth/access_token"
            ) {
                header(HttpHeaders.ContentType, ContentType.Application.Json)
                setBody(
                    body = GetAccessTokenRequestBody(
                        clientId = CLIENT_ID,
                        clientSecret = CLIENT_SECRET,
                        code = code
                    )
                )
            }
            request.body()
        }
    }
}
