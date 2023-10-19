package com.borred.ktor_client.network

import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.header

internal fun getHttpClient(): HttpClient {
    return HttpClient(Android) {
        install(Logging){
            level = LogLevel.ALL
        }
        install(DefaultRequest) {
            url("https://api.github.com/")
            header(key = "X-GitHub-Api-Version", value = "2022-11-28")
        }
    }
}
