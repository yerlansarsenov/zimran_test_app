package com.borred.zimran_test_app.repositories

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.borred.zimran_test_app.repositories.model.GitRepository

@Composable
fun RepositoriesHistoryScreen(
    onGoToDetails: (GitRepository) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxSize()
    ) {
        Text(
            text = "Search Repos History"
        )
        Button(
            onClick = { onGoToDetails(GitRepository(name = "sample")) }
        ) {
            Text(
                text = "Details"
            )
        }
    }
}
