package com.borred.zimran_test_app.auth

import androidx.compose.runtime.Stable

@Stable
sealed class AuthScreenState(val route: String) {
    // splashScreen
    object Loading : AuthScreenState("loading")

    object NotAuthorized : AuthScreenState("not_authorized")

    object Main : AuthScreenState("main")
}