package com.borred.zimran_test_app.repodetails

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.borred.ktor_client.network.search.repos.model.GitRepository
import com.borred.ktor_client.network.search.users.model.GitUser
import com.borred.zimran_test_app.repositories.format
import com.borred.zimran_test_app.ui.AuthorInfo
import com.borred.zimran_test_app.ui.DefinitionAndInfoRow
import com.borred.zimran_test_app.ui.Header
import kotlinx.collections.immutable.persistentMapOf

@Composable
fun RepoDetailsScreen(
    gitRepository: GitRepository,
    onGoToUserRepos: (GitUser) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        Header(
            title = gitRepository.name
        )
        Spacer(modifier = Modifier.height(32.dp))
        val authorCardShape = RoundedCornerShape(16.dp)
        Box(
            modifier = Modifier
                .padding(4.dp)
                .clip(authorCardShape)
                .border(
                    width = 2.dp,
                    color = Color.Black,
                    shape = authorCardShape
                )
                .clickable { onGoToUserRepos(gitRepository.owner) }
        ) {
            AuthorInfo(owner = gitRepository.owner)
        }
        gitRepository.description?.let {
            Text(
                text = it,
                style = MaterialTheme.typography.headlineSmall,
                color = Color.DarkGray,
                modifier = Modifier.padding(horizontal = 4.dp)
            )
        }
        remember(gitRepository) {
            persistentMapOf(
                "forks" to "${gitRepository.forksCount}",
                "issues" to "${gitRepository.openIssuesCount}",
                "stars" to "${gitRepository.stargazersCount}",
                "created at" to gitRepository.createdAt.format(),
                "updated at" to gitRepository.updatedAt.format()
            )
        }.forEach { (definition, info) ->
            DefinitionAndInfoRow(
                definition = definition,
                info = info,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp)
            )
            Divider()
        }
    }
}
