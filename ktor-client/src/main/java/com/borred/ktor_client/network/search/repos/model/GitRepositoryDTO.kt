package com.borred.ktor_client.network.search.repos.model

import com.borred.ktor_client.network.search.users.model.GitUser
import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable

@Serializable
data class GitRepositoryDTO(
    val id: Int?,
    val name: String?,
    val description: String?,
    val createdAt: Instant?,
    val updatedAt: Instant?,
    val language: String?,
    val forksCount: Int?,
    val stargazersCount: Int?,
    val openIssuesCount: Int?,
    val owner: GitUser?
) {
    fun toDomain(): GitRepository {
        return GitRepository(
            id = id!!,
            name = name!!,
            description = description,
            createdAt = createdAt!!,
            updatedAt = updatedAt!!,
            language = language,
            forksCount = forksCount!!,
            stargazersCount = stargazersCount!!,
            openIssuesCount = openIssuesCount!!,
            owner = owner!!,
        )
    }
}
