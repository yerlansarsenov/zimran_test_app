package com.borred.zimran_test_app.users

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Divider
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
import androidx.compose.ui.composed
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.collectAsLazyPagingItems
import com.borred.zimran_test_app.ui.AuthorInfo
import com.borred.zimran_test_app.ui.DisplayAndHeadline
import com.borred.zimran_test_app.ui.Header
import com.borred.zimran_test_app.ui.PagingLazyColumn
import com.borred.zimran_test_app.ui.SortKindDialog
import com.borred.zimran_test_app.ui.seenColor
import com.borred.zimran_test_app.users.model.GitUserUI

@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterial3Api::class)
@Composable
fun SearchUsersScreen(
    onGoToUserRepos: (GitUserUI) -> Unit,
    onGoToHistory: () -> Unit,
    onShowError: (Throwable) -> Unit,
    modifier: Modifier = Modifier
) {
    val viewModel = hiltViewModel<SearchUsersViewModel>()
    var isSortDialogVisible by remember {
        mutableStateOf(false)
    }
    Column(
        modifier = modifier.fillMaxSize()
    ) {
        Header(
            title = "Search for users",
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
            DisplayAndHeadline(
                display = "Start typing..",
                headline = "Who are you searching for?"
            )
        } else {
            PagingLazyColumn(
                paginatedListItems = viewModel.gitUsersPaging.value.collectAsLazyPagingItems(),
                onShowError = onShowError
            ) { item ->
                AuthorInfo(
                    owner = item,
                    modifier = Modifier
                        .composed {
                            if (item.isSeen) {
                                background(seenColor)
                            } else this
                        }
                        .clickable {
                            viewModel.addUserToHistory(item)
                            onGoToUserRepos(item)
                        }
                )
                Divider()
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
