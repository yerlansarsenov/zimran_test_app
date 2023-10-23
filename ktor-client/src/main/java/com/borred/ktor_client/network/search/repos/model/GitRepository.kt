package com.borred.ktor_client.network.search.repos.model

import androidx.compose.runtime.Stable
import com.borred.ktor_client.network.search.users.model.GitUser
import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable

@Stable
@Serializable
data class GitRepository(
    val id: Int,
    val name: String,
    val description: String?,
    val createdAt: Instant,
    val updatedAt: Instant,
    val language: String?,
    val forksCount: Int,
    val stargazersCount: Int,
    val openIssuesCount: Int,
    val owner: GitUser
)
