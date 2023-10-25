package com.borred.zimran_test_app.users.model

import androidx.compose.runtime.Stable
import com.borred.ktor_client.network.search.users.model.GitUser
import kotlinx.serialization.Serializable

@Stable
@Serializable
data class GitUserUI(
    val id: Int,
    val login: String,
    val avatarUrl: String,
    val isSeen: Boolean
)

fun GitUser.toUI(isSeen: Boolean = false): GitUserUI {
    return GitUserUI(
        id = id,
        login = login,
        avatarUrl = avatarUrl,
        isSeen = isSeen
    )
}

fun GitUserUI.toDomain(): GitUser {
    return GitUser(
        id = id,
        login = login,
        avatarUrl = avatarUrl
    )
}
