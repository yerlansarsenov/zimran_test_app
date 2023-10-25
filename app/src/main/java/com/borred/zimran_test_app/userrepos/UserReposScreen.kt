package com.borred.zimran_test_app.userrepos

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.collectAsLazyPagingItems
import com.borred.ktor_client.network.search.repos.model.GitRepository
import com.borred.zimran_test_app.repositories.model.GitRepositoryUI
import com.borred.zimran_test_app.ui.GitRepositoryView
import com.borred.zimran_test_app.ui.Header
import com.borred.zimran_test_app.ui.PagingLazyColumn
import com.borred.zimran_test_app.ui.SortKindDialog

@Composable
fun UserReposScreen(
    onGoToDetails: (GitRepositoryUI) -> Unit,
    onShowError: (Throwable) -> Unit,
    modifier: Modifier = Modifier
) {
    val viewModel = hiltViewModel<UserReposViewModel>()
    var isSortDialogVisible by remember {
        mutableStateOf(false)
    }
    Column(
        modifier = modifier.fillMaxSize()
    ) {
        Header(
            title = "${viewModel.user?.login}'s repos",
            actionButtonIcon = Icons.Default.Settings,
            onClickAction = { isSortDialogVisible = true }
        )
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
    SortKindDialog(
        isVisible = isSortDialogVisible,
        chosenOne = viewModel.sortByFlow.collectAsState().value,
        onChoose = viewModel::setSortBy,
        onDismiss = { isSortDialogVisible = false }
    )
}
