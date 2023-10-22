package com.borred.zimran_test_app.search

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun SearchScreen(
    onGoToRepos: () -> Unit,
    onGoToUsers: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceAround,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(
            onClick = onGoToRepos
        ) {
            Text(text = "Repositories")
        }
        Button(
            onClick = onGoToUsers
        ) {
            Text(text = "Users")
        }
    }
}
