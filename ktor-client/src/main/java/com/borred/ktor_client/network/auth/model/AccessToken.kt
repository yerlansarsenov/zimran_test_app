package com.borred.ktor_client.network.auth.model

import kotlinx.serialization.Serializable

@Serializable
data class AccessToken(
    val accessToken: String,
    val tokenType: String
)
