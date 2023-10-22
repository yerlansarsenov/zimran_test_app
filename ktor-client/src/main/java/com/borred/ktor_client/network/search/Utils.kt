package com.borred.ktor_client.network.search

import com.borred.ktor_client.local.auth.AccessTokenDataStore
import com.borred.ktor_client.network.auth.UnauthorizedException
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.header
import kotlinx.coroutines.flow.firstOrNull

internal suspend fun HttpRequestBuilder.setToken(
    tokenDataStore: AccessTokenDataStore
) {
    val token = tokenDataStore.getAccessTokenFlow().firstOrNull() ?: throw UnauthorizedException()
    this.header("Authorization", "${token.tokenType} ${token.accessToken}")
}
