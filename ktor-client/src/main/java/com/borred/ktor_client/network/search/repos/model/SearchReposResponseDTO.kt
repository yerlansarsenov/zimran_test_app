package com.borred.ktor_client.network.search.repos.model

import kotlinx.serialization.Serializable

@Serializable
data class SearchReposResponseDTO(
    val totalCount: Int,
    val items: List<GitRepositoryDTO>
) {
    fun toDomain(): SearchReposResponse {
        return SearchReposResponse(
            totalCount = totalCount,
            items = items.safeMap { it.toDomain() }
        )
    }
}

fun <T, R> List<T>.safeMap(mapper: (T) -> R): List<R> {
    return mapNotNull {
        kotlin.runCatching {
            mapper(it)
        }.getOrNull()
    }
}
