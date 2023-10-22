package com.borred.zimran_test_app

import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.borred.zimran_test_app.repodetails.RepoDetailsScreen
import com.borred.zimran_test_app.repositories.RepositoriesHistoryScreen
import com.borred.zimran_test_app.repositories.SearchRepositoriesScreen
import com.borred.zimran_test_app.repositories.model.GitRepository
import com.borred.zimran_test_app.search.SearchScreen
import com.borred.zimran_test_app.ui.MainScreenRoutes
import com.borred.zimran_test_app.userrepos.UserReposScreen
import com.borred.zimran_test_app.users.SearchUsersScreen
import com.borred.zimran_test_app.users.UsersHistoryScreen
import kotlinx.serialization.json.Json

@Composable
fun MainScreen() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = MainScreenRoutes.Search.route,
    ) {
        mainScreenNavGraph(navController)
    }
}

private fun NavGraphBuilder.mainScreenNavGraph(
    navController: NavHostController
) {
    composable(
        route = MainScreenRoutes.Search.route,
        enterTransition = {
            slideInHorizontally(
                animationSpec = tween(400),
                initialOffsetX = { -it / 2 }
            )
        },
        exitTransition = {
            slideOutHorizontally(
                animationSpec = tween(400),
                targetOffsetX = { -it / 2 }
            )
        }
    ) {
        SearchScreen(
            onGoToRepos = {
                navController.navigate(MainScreenRoutes.Repositories.route)
            },
            onGoToUsers = {
                navController.navigate(MainScreenRoutes.Users.route)
            }
        )
    }

    composable(
        route = MainScreenRoutes.Repositories.route,
        enterTransition = {
            slideInHorizontally(
                animationSpec = tween(400),
                initialOffsetX = { -it / 2 }
            )
        },
        exitTransition = {
            slideOutHorizontally(
                animationSpec = tween(400),
                targetOffsetX = { -it / 2 }
            )
        }
    ) {
        SearchRepositoriesScreen(
            onGoToDetails = { repo ->
                navController.navigate(MainScreenRoutes.RepoDetails.destinationRoute(repo))
            },
            onGoToHistory = {
                navController.navigate(MainScreenRoutes.RepoHistory.route)
            }
        )
    }

    composable(
        route = MainScreenRoutes.Users.route,
        enterTransition = {
            slideInHorizontally(
                animationSpec = tween(400),
                initialOffsetX = { -it / 2 }
            )
        },
        exitTransition = {
            slideOutHorizontally(
                animationSpec = tween(400),
                targetOffsetX = { -it / 2 }
            )
        }
    ) {
        SearchUsersScreen(
            onGoToUserRepos = { id ->
                navController.navigate(MainScreenRoutes.UserRepos.destinationRoute(id))
            },
            onGoToHistory = {
                navController.navigate(MainScreenRoutes.UsersHistory.route)
            }
        )
    }

    composable(
        route = MainScreenRoutes.RepoDetails.route,
        enterTransition = {
            slideInHorizontally(
                animationSpec = tween(400),
                initialOffsetX = { -it / 2 }
            )
        },
        exitTransition = {
            slideOutHorizontally(
                animationSpec = tween(400),
                targetOffsetX = { -it / 2 }
            )
        }
    ) { backStackEntry ->
        val repoJson = backStackEntry.arguments?.getString("repo") ?: return@composable
        val gitRepository = Json.decodeFromString<GitRepository>(repoJson)
        RepoDetailsScreen(
            gitRepository = gitRepository,
            onGoToUserRepos = { id ->
                navController.navigate(MainScreenRoutes.UserRepos.destinationRoute(id))
            }
        )
    }

    composable(
        route = MainScreenRoutes.UserRepos.route,
        enterTransition = {
            slideInHorizontally(
                animationSpec = tween(400),
                initialOffsetX = { -it / 2 }
            )
        },
        exitTransition = {
            slideOutHorizontally(
                animationSpec = tween(400),
                targetOffsetX = { -it / 2 }
            )
        }
    ) { backStackEntry ->
        val userId = backStackEntry.arguments?.getInt("id") ?: return@composable
        UserReposScreen(
            userId = userId,
            onGoToDetails = { repo ->
                navController.navigate(MainScreenRoutes.RepoDetails.destinationRoute(repo))
            }
        )
    }

    composable(
        route = MainScreenRoutes.RepoHistory.route,
        enterTransition = {
            slideInHorizontally(
                animationSpec = tween(400),
                initialOffsetX = { -it / 2 }
            )
        },
        exitTransition = {
            slideOutHorizontally(
                animationSpec = tween(400),
                targetOffsetX = { -it / 2 }
            )
        }
    ) {
        RepositoriesHistoryScreen(
            onGoToDetails = { repo ->
                navController.navigate(MainScreenRoutes.RepoDetails.destinationRoute(repo))
            }
        )
    }

    composable(
        route = MainScreenRoutes.UsersHistory.route,
        enterTransition = {
            slideInHorizontally(
                animationSpec = tween(400),
                initialOffsetX = { -it / 2 }
            )
        },
        exitTransition = {
            slideOutHorizontally(
                animationSpec = tween(400),
                targetOffsetX = { -it / 2 }
            )
        }
    ) {
        UsersHistoryScreen(
            onGoToUserRepos = { id ->
                navController.navigate(MainScreenRoutes.UserRepos.destinationRoute(id))
            }
        )
    }
}
