package com.borred.zimran_test_app.users

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import kotlinx.collections.immutable.persistentListOf

@Composable
fun BottomNav(
    selectedItem: BottomNavItem,
    onClickItem: (BottomNavItem) -> Unit
) {
    val items = persistentListOf(
        BottomNavItem.Repositories,
        BottomNavItem.Users,
        BottomNavItem.MyProfile
    )
    NavigationBar {
        items.forEach { 
            NavigationBarItem(
                selected = selectedItem == it,
                onClick = { onClickItem(it) },
                icon = {
                    Icon(
                        painter = rememberVectorPainter(image = it.icon),
                        contentDescription = null
                    )
                }
            )
        }
    }
}
