package com.borred.zimran_test_app.auth

import android.app.Activity
import android.content.Intent
import androidx.core.net.toUri
import com.borred.ktor_client.network.CLIENT_ID

// Github OAuth app client ID
private const val LOGIN_URL = "https://github.com/login/oauth/authorize"
private const val AUTH_CALLBACK_URL = "zimrann://callback"

internal fun Activity.authorizeViaGithub() {
    val loginUri = buildString {
        append(LOGIN_URL)
        append("?client_id=")
        append(CLIENT_ID)
        append("&scope=repo")
        append("&redirect_url=")
        append(AUTH_CALLBACK_URL)
    }.toUri()
    val intent = Intent(
        Intent.ACTION_VIEW,
        loginUri
    )
    startActivity(intent)
}
