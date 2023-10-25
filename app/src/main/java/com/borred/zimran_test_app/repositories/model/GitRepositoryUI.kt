package com.borred.zimran_test_app.repositories.model

import androidx.compose.runtime.Stable
import com.borred.ktor_client.network.search.repos.model.GitRepository
import com.borred.ktor_client.network.search.users.model.GitUser
import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable

@Stable
@Serializable
data class GitRepositoryUI(
    val id: Int,
    val name: String,
    val description: String?,
    val createdAt: Instant,
    val updatedAt: Instant,
    val language: String?,
    val forksCount: Int,
    val stargazersCount: Int,
    val openIssuesCount: Int,
    val owner: GitUser,
    val isSeen: Boolean
)

fun GitRepository.toUI(isSeen: Boolean = false): GitRepositoryUI {
    return GitRepositoryUI(
        id = id,
        name = name,
        description = description,
        createdAt = createdAt,
        updatedAt = updatedAt,
        language = language,
        forksCount = forksCount,
        stargazersCount = stargazersCount,
        openIssuesCount = openIssuesCount,
        owner = owner,
        isSeen = isSeen
    )
}

fun GitRepositoryUI.toDomain(): GitRepository {
    return GitRepository(
        id = id,
        name = name,
        description = description,
        createdAt = createdAt,
        updatedAt = updatedAt,
        language = language,
        forksCount = forksCount,
        stargazersCount = stargazersCount,
        openIssuesCount = openIssuesCount,
        owner = owner,
    )
}
