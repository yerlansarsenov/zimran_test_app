package com.borred.zimran_test_app.ui

import android.util.Log
import com.borred.ktor_client.network.search.repos.model.GitRepository
import com.borred.ktor_client.network.search.users.model.GitUser
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

sealed class MainScreenRoutes(val route: String) {

    object Search : MainScreenRoutes("search")

    object Repositories : MainScreenRoutes("repositories")

    object Users : MainScreenRoutes("users")

    object UserRepos : MainScreenRoutes("user/repositories?user={user}") {

        fun destinationRoute(user: GitUser): String {
            return route.replace("{user}", Json.encodeToString(user))
        }
    }

    object RepoHistory : MainScreenRoutes("repositories/history")

    object UsersHistory : MainScreenRoutes("users/history")

    object RepoDetails : MainScreenRoutes("repositories/details?repo={repo}") {

        fun destinationRoute(repo: GitRepository): String {
            val str = Json.encodeToString(repo)
            Log.e("HERE!!", "destinationRoute: str = $str")
            return route.replace("{repo}", str)
        }
    }
}
