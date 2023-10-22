package com.borred.ktor_client.network.auth.model

import kotlinx.serialization.Serializable

@Serializable
data class GetAccessTokenRequestBody(
    val clientId: String,
    val clientSecret: String,
    val code: String
)
