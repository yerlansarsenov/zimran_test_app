package com.borred.ktor_client.network

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.engine.HttpClientEngineConfig
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.ANDROID
import io.ktor.client.plugins.logging.DEFAULT
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.header
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonNamingStrategy
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Singleton
    @Provides
    @Named(AUTH_CLIENT)
    fun provideAuthHttpClient(): HttpClient {
        return getAuthHttpClient()
    }

    @Singleton
    @Provides
    @Named(SEARCH_CLIENT)
    fun provideSearchHttpClient(): HttpClient {
        return getSearchHttpClient()
    }
}

private fun getAuthHttpClient(): HttpClient {
    return HttpClient(Android) {
        installLogAndJson()
        install(DefaultRequest) {
            url("https://github.com/")
            header(key = "X-GitHub-Api-Version", value = "2022-11-28")
        }
    }
}

private fun getSearchHttpClient(): HttpClient {
    return HttpClient(Android) {
        installLogAndJson()
        install(DefaultRequest) {
            url("https://api.github.com/")
            header(key = "X-GitHub-Api-Version", value = "2022-11-28")
        }
    }
}

@OptIn(ExperimentalSerializationApi::class)
private fun <T : HttpClientEngineConfig> HttpClientConfig<T>.installLogAndJson() {
    install(Logging){
        logger = Logger.DEFAULT
        level = LogLevel.ALL
    }
    install(ContentNegotiation) {
        json(
            Json {
                prettyPrint = true
                namingStrategy = JsonNamingStrategy.SnakeCase
                ignoreUnknownKeys = true
            }
        )
    }
}
