package com.borred.zimran_test_app.repodetails

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.borred.zimran_test_app.repositories.model.GitRepository

@Composable
fun RepoDetailsScreen(
    gitRepository: GitRepository,
    onGoToUserRepos: (id: Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxSize()
    ) {
        Text(
            text = "Repo Details"
        )
        Text(
            text = "name: ${gitRepository.name}"
        )
        Button(
            onClick = { onGoToUserRepos(1) }
        ) {
            Text(
                text = "This User's Repos"
            )
        }
    }
}
