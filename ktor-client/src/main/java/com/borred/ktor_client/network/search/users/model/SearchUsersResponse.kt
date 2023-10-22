package com.borred.ktor_client.network.search.users.model

import com.borred.ktor_client.network.search.users.model.GitUser
import kotlinx.serialization.Serializable

@Serializable
data class SearchUsersResponse(
    val totalCount: Int,
    val items: List<GitUser>
)
