package com.borred.zimran_test_app.search

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.borred.zimran_test_app.ui.Header

@Composable
fun SearchScreen(
    onGoToRepos: () -> Unit,
    onGoToUsers: () -> Unit,
    onLogOut: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxSize()
    ) {
        Header(title = "Search among...")
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(32.dp))
            RouteButton(
                type = RouteButtonType.Primary,
                title = "Repositories",
                onClick = onGoToRepos
            )
            RouteButton(
                type = RouteButtonType.Primary,
                title = "Users",
                onClick = onGoToUsers
            )
            RouteButton(
                type = RouteButtonType.Secondary,
                title = "Log out",
                onClick = onLogOut
            )
        }
    }
}
