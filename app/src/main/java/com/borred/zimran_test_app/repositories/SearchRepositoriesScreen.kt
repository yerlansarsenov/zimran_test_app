package com.borred.zimran_test_app.repositories

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.collectAsLazyPagingItems
import com.borred.zimran_test_app.repositories.model.GitRepositoryUI
import com.borred.zimran_test_app.ui.DisplayAndHeadline
import com.borred.zimran_test_app.ui.GitRepositoryView
import com.borred.zimran_test_app.ui.Header
import com.borred.zimran_test_app.ui.PagingLazyColumn
import com.borred.zimran_test_app.ui.SortKindDialog
import com.borred.zimran_test_app.users.model.GitUserUI
import kotlinx.datetime.Clock
import kotlin.time.Duration.Companion.days

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun SearchRepositoriesScreen(
    onGoToDetails: (GitRepositoryUI) -> Unit,
    onGoToHistory: () -> Unit,
    onShowError: (Throwable) -> Unit,
    modifier: Modifier = Modifier
) {
    val viewModel = hiltViewModel<SearchRepositoriesViewModel>()
    var isSortDialogVisible by remember {
        mutableStateOf(false)
    }
    Column(
        modifier = modifier.fillMaxSize()
    ) {
        Header(
            title = "Search for repositories",
            actionButtonIcon = Icons.Default.Refresh,
            onClickAction = onGoToHistory,
            secondaryActionButtonIcon = Icons.Default.Settings,
            onClickSecondaryAction = { isSortDialogVisible = true }
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
            Spacer(modifier = Modifier.height(32.dp))
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
                        .clickable {
                            viewModel.addToHistory(item)
                            onGoToDetails(item)
                        }
                )
            }
        }
    }
    SortKindDialog(
        isVisible = isSortDialogVisible,
        chosenOne = viewModel.sortByFlow.collectAsState().value,
        onChoose = viewModel::setSortBy,
        onDismiss = { isSortDialogVisible = false }
    )
}

@Preview
@Composable
private fun GitRepositoryView_Preview() {
    GitRepositoryView(
        item = GitRepositoryUI(
            id = 0,
            name = "Zimran Application",
            description = "some loooong description",
            createdAt = Clock.System.now().minus(10.days),
            updatedAt = Clock.System.now().minus(8.days),
            language = "kotlin",
            forksCount = 32,
            stargazersCount = 324,
            openIssuesCount = 321,
            owner = GitUserUI(
                id = 0,
                login = "YerlanSarsenov",
                avatarUrl = "",
                isSeen = false
            ),
            isSeen = false
        ),
        modifier = Modifier
    )
}
