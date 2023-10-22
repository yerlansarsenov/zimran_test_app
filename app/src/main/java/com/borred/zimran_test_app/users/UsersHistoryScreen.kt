package com.borred.zimran_test_app.users

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun UsersHistoryScreen(
    onGoToUserRepos: (id: Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxSize()
    ) {
        Text(
            text = "Search Users History"
        )
        Button(
            onClick = { onGoToUserRepos(1) }
        ) {
            Text(
                text = "User Repos"
            )
        }
    }
}
