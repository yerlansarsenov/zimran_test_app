package com.borred.zimran_test_app.users

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController

@Composable
fun TabsScreen(
    onShowError: (Throwable) -> Unit,
    modifier: Modifier = Modifier
) {
    val navController = rememberNavController()
    Column(
        modifier = modifier
    ) {
        val item = rememberSaveable(stateSaver = BottomNavItem.Saver()) {
            mutableStateOf(BottomNavItem.Repositories)
        }

        BottomNav(
            selectedItem = item.value,
            onClickItem = { item.value = it }
        )
    }
}

private fun NavGraphBuilder.mainScreenNavGraph(
    navController: NavHostController,
    onShowError: (Throwable) -> Unit
) {

}
