package com.borred.zimran_test_app.repositories

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.AsyncImage
import com.borred.ktor_client.network.search.repos.model.GitRepository
import com.borred.ktor_client.network.search.users.model.GitUser
import com.borred.zimran_test_app.prettyNumber
import com.borred.zimran_test_app.ui.DisplayAndHeadline
import com.borred.zimran_test_app.ui.Header
import com.borred.zimran_test_app.ui.PagingLazyColumn
import kotlinx.datetime.Clock
import kotlin.time.Duration.Companion.days

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun SearchRepositoriesScreen(
    onGoToDetails: (GitRepository) -> Unit,
    onGoToHistory: () -> Unit,
    onShowError: (Throwable) -> Unit,
    modifier: Modifier = Modifier
) {
    val viewModel = hiltViewModel<SearchRepositoriesViewModel>()
    Column(
        modifier = modifier.fillMaxSize()
    ) {
        Header(
            title = "Search for repositories",
            actionButtonIcon = Icons.Default.Refresh,
            onClickAction = onGoToHistory
        )
        val searchText by viewModel.searchTextFlow.collectAsState()
        val keyboard = LocalSoftwareKeyboardController.current
        TextField(
            value = searchText,
            onValueChange = viewModel::setSearchText,
            modifier = Modifier
                .padding(horizontal = 8.dp)
                .fillMaxWidth(),
            keyboardActions = KeyboardActions(
                onSearch = {
                    keyboard?.hide()
                }
            ),
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Search
            )
        )
        if (searchText.isEmpty()) {
            DisplayAndHeadline(
                display = "Start typing..",
                headline = "What repository are you searching for?"
            )
        } else {
            PagingLazyColumn(
                paginatedListItems = viewModel.gitReposPaging.value.collectAsLazyPagingItems(),
                onShowError = onShowError
            ) { item ->
                GitRepositoryView(
                    item = item,
                    modifier = Modifier
                        .clickable { onGoToDetails(item) }
                )
            }
        }
    }
}

@Composable
private fun GitRepositoryView(
    item: GitRepository,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min)
                .padding(vertical = 8.dp)
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            AsyncImage(
                model = item.owner.avatarUrl,
                contentDescription = null,
                modifier = Modifier
                    .clip(CircleShape)
                    .size(70.dp)
            )
            Column(
                verticalArrangement = Arrangement.SpaceAround,
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(1f)
            ) {
                Text(
                    text = item.name,
                    style = MaterialTheme.typography.titleLarge
                )
                Text(
                    text = item.owner.login,
                    style = MaterialTheme.typography.bodyMedium
                )
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text(
                        text = remember(item) {
                            item.updatedAt.format()
                        },
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalAlignment = Alignment.End
            ) {
                CountAndIcon(
                    count = item.stargazersCount,
                    icon = Icons.Default.Star,
                    tint = Color.Yellow
                )
                CountAndIcon(
                    count = item.forksCount,
                    icon = Icons.Default.Share,
                    tint = Color.DarkGray
                )
                CountAndIcon(
                    count = item.openIssuesCount,
                    icon = Icons.Default.ExitToApp,
                    tint = Color.Red
                )
            }
        }
        Divider()
    }
}

@Composable
private fun CountAndIcon(
    count: Int,
    icon: ImageVector,
    tint: Color,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(
            text = count.prettyNumber(),
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.alignByBaseline()
        )
        Icon(
            painter = rememberVectorPainter(icon),
            contentDescription = "stars",
            tint = tint,
            modifier = Modifier.size(16.dp)
        )
    }
}

@Preview
@Composable
private fun GitRepositoryView_Preview() {
    GitRepositoryView(
        item = GitRepository(
            id = 0,
            name = "Zimran Application",
            description = "some loooong description",
            createdAt = Clock.System.now().minus(10.days),
            updatedAt = Clock.System.now().minus(8.days),
            language = "kotlin",
            forksCount = 32,
            stargazersCount = 324,
            openIssuesCount = 321,
            owner = GitUser(
                id = 0,
                login = "YerlanSarsenov",
                avatarUrl = ""
            )
        ),
        modifier = Modifier
    )
}
