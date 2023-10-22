package com.borred.ktor_client.network.search.users.model

import kotlinx.serialization.Serializable

@Serializable
data class GitUser(
    val id: Int,
    val login: String,
    val avatarUrl: String
)
