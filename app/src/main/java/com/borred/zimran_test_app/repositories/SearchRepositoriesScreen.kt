package com.borred.zimran_test_app.repositories

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemContentType
import androidx.paging.compose.itemKey
import coil.compose.AsyncImage
import com.borred.ktor_client.network.search.repos.model.GitRepository
import com.borred.zimran_test_app.ui.CircularPaginationLoaderItem
import com.borred.zimran_test_app.ui.DisplayAndHeadline
import com.borred.zimran_test_app.ui.EmptyStateView
import com.borred.zimran_test_app.ui.ErrorStateView
import com.borred.zimran_test_app.ui.Header
import com.borred.zimran_test_app.ui.LoadingView
import com.borred.zimran_test_app.ui.PagingLazyColumn

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
                paginatedListItems = viewModel.gitReposPaging.collectAsLazyPagingItems(),
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
    Column(modifier = modifier) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
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
                verticalArrangement = Arrangement.SpaceAround
            ) {
                Text(
                    text = item.name,
                    style = MaterialTheme.typography.titleLarge
                )
                Text(
                    text = item.owner.login,
                    style = MaterialTheme.typography.headlineSmall
                )
            }
        }
        Divider()
    }
}
