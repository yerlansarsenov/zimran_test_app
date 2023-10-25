package com.borred.zimran_test_app.users

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Divider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.borred.zimran_test_app.ui.AuthorInfo
import com.borred.zimran_test_app.ui.DisplayAndHeadline
import com.borred.zimran_test_app.ui.Header
import com.borred.zimran_test_app.ui.LoadingView
import com.borred.zimran_test_app.users.model.GitUserUI

@Composable
fun UsersHistoryScreen(
    onGoToUserRepos: (GitUserUI) -> Unit,
    modifier: Modifier = Modifier
) {
    val viewModel = hiltViewModel<UsersHistoryViewModel>()
    Column(
        modifier = modifier.fillMaxSize()
    ) {
        Header(
            title = "Users history",
            actionButtonIcon = Icons.Default.Delete,
            onClickAction = viewModel::deleteHistory
        )
        Box(modifier = Modifier.fillMaxSize()) {
            val state = viewModel.historyListState.value
            when (state) {
                HistoryState.Empty -> {
                    DisplayAndHeadline(
                        display = "Empty history",
                        headline = "start searching repositories",
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                is HistoryState.List -> {
                    LazyColumn(
                        modifier = Modifier.matchParentSize()
                    ) {
                        items(state.list) { item ->
                            AuthorInfo(
                                owner = item,
                                modifier = Modifier
                                    .clickable {
                                        onGoToUserRepos(item)
                                    }
                            )
                            Divider()
                        }
                    }
                }
                HistoryState.Loading -> {
                    LoadingView()
                }
            }
        }
    }
}
