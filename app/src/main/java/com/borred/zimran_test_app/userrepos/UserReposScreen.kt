package com.borred.zimran_test_app.userrepos

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.borred.ktor_client.network.search.repos.model.GitRepository
import com.borred.ktor_client.network.search.users.model.GitUser

@Composable
fun UserReposScreen(
    user: GitUser,
    onGoToDetails: (GitRepository) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxSize()
    ) {
        Text(
            text = "User Repos"
        )
        Button(
            onClick = {
//                onGoToDetails(GitRepository(name = "sample"))
            }
        ) {
            Text(
                text = "His Repos Details"
            )
        }
    }
}
