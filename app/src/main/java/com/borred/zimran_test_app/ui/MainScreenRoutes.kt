package com.borred.zimran_test_app.ui

import android.util.Log
import com.borred.ktor_client.network.search.users.model.GitUser
import com.borred.zimran_test_app.repositories.model.GitRepositoryUI
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

internal val SpecialCharsMap = mapOf<String, String>(
    "#" to "§sharp§",
    "&" to "§amp§"
)

sealed class MainScreenRoutes(val route: String) {

    object Search : MainScreenRoutes("search")

    object Repositories : MainScreenRoutes("repositories")

    object Users : MainScreenRoutes("users")

    object UserRepos : MainScreenRoutes("user/repositories?user={user}") {

        fun destinationRoute(user: GitUser): String {
            var str = Json.encodeToString(user)
            SpecialCharsMap.forEach {
                str = str.replace(it.key, it.value)
            }
            return route.replace("{user}", str)
        }
    }

    object RepoHistory : MainScreenRoutes("repositories/history")

    object UsersHistory : MainScreenRoutes("users/history")

    object RepoDetails : MainScreenRoutes("repositories/details?repo={repo}") {

        fun destinationRoute(repo: GitRepositoryUI): String {
            var str = Json.encodeToString(repo)
            SpecialCharsMap.forEach {
                str = str.replace(it.key, it.value)
            }
            Log.e("HERE!!", "destinationRoute: str = $str")
            return route.replace("{repo}", str)
        }
    }
}
