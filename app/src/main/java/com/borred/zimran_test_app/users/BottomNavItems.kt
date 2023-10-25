package com.borred.zimran_test_app.users

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AccountCircle
import androidx.compose.material.icons.rounded.Search
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.mapSaver
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomNavItem(
    val title: String,
    val icon: ImageVector,
    val screenRoute: String
) {

    object Repositories : BottomNavItem("Repositories", Icons.Rounded.Search, "home")

    object Users : BottomNavItem("Users", Icons.Rounded.Search, "my_network")

    object MyProfile : BottomNavItem("My Profile", Icons.Rounded.AccountCircle, "add_post")

    companion object {
        fun Saver(): Saver<BottomNavItem, Any> {
            val key = "item"
            return mapSaver(
                save = {
                    when (it) {
                        Repositories -> mapOf(key to 1)
                        Users -> mapOf(key to 2)
                        MyProfile -> mapOf(key to 3)
                    }
                },
                restore = {
                    when (it[key] as Int) {
                        1 -> Repositories
                        2 -> Users
                        3 -> MyProfile
                        else -> Repositories
                    }
                }
            )
        }
    }
}
