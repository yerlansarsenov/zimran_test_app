package com.borred.ktor_client.network.search.users.model

enum class UsersSort(internal val value: String) {
    FOLLOWERS("followers"),
    REPOS("repositories"),
    JOINED("joined")
}
