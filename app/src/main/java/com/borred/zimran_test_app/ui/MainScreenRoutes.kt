package com.borred.zimran_test_app.ui

import com.borred.ktor_client.network.search.repos.model.GitRepository
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

sealed class MainScreenRoutes(val route: String) {

    object Search : MainScreenRoutes("search")

    object Repositories : MainScreenRoutes("repositories")

    object Users : MainScreenRoutes("users")

    object UserRepos : MainScreenRoutes("user/repositories?id={userId}") {

        fun destinationRoute(userId: Int): String {
            return route.replace("{userId}", "$userId")
        }
    }

    object RepoHistory : MainScreenRoutes("repositories/history")

    object UsersHistory : MainScreenRoutes("users/history")

    object RepoDetails : MainScreenRoutes("repositories/details?repo={repo}") {

        fun destinationRoute(repo: GitRepository): String {
            return route.replace("{repo}", Json.encodeToString(repo))
        }
    }
}
