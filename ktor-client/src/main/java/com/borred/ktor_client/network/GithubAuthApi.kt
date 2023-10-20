package com.borred.ktor_client.network

import com.borred.ktor_client.network.client_secret.CLIENT_SECRET
import com.borred.ktor_client.network.model.AccessToken
import com.borred.ktor_client.network.model.GetAccessTokenRequestBody
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.contentType
import javax.inject.Inject

@Module
@InstallIn(ViewModelComponent::class)
object GithubAuthApiModule {

    @Provides
    @ViewModelScoped
    internal fun provideGithubAuthApi(
        impl: GithubAuthApiImpl
    ): GithubAuthApi {
        return impl
    }
}

interface GithubAuthApi {

    suspend fun getAccessToken(
        code: String
    ) : Result<AccessToken>
}

internal class GithubAuthApiImpl
@Inject
constructor(
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
