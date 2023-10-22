package com.borred.ktor_client.network.search.repos.model

data class SearchReposResponse(
    val totalCount: Int,
    val items: List<GitRepository>
)
