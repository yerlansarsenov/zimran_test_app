package com.borred.ktor_client.network.search.repos.model

import com.borred.ktor_client.network.search.users.model.GitUser
import kotlinx.datetime.Instant

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
