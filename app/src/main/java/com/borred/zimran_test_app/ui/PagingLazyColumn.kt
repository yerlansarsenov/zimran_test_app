package com.borred.zimran_test_app.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.itemContentType
import androidx.paging.compose.itemKey

@Composable
fun <T : Any> PagingLazyColumn(
    paginatedListItems: LazyPagingItems<T>,
    onShowError: (Throwable) -> Unit,
    modifier: Modifier = Modifier,
    itemContent: @Composable (T) -> Unit
) {
    val refreshLoadState = paginatedListItems.loadState.refresh
    val appendLoadState = paginatedListItems.loadState.append
    LaunchedEffect(key1 = refreshLoadState) {
        if (refreshLoadState is LoadState.Error) {
            onShowError(refreshLoadState.error)
        }
    }
    LazyColumn(
        state = paginatedListItems.rememberLazyListState2(),
        modifier = modifier.fillMaxSize()
    ) {
        val itemCount = paginatedListItems.itemCount
        val isEmpty = refreshLoadState is LoadState.NotLoading &&
                itemCount <= 0 &&
                appendLoadState.endOfPaginationReached
        items(
            count = paginatedListItems.itemCount,
            key = paginatedListItems.itemKey(),
            contentType = paginatedListItems.itemContentType { it.javaClass }
        ) { index ->
            val item = paginatedListItems[index] ?: return@items
            itemContent(item)
        }
        if (paginatedListItems.loadState.append is LoadState.Loading) {
            item {
                CircularPaginationLoaderItem()
            }
        }
        when {
            refreshLoadState is LoadState.Loading -> {
                item {
                    LoadingView(
                        modifier = Modifier.fillParentMaxSize()
                    )
                }
            }

            refreshLoadState is LoadState.Error -> {
                item {
                    ErrorStateView(
                        modifier = Modifier.fillParentMaxSize()
                    )
                }
            }

            isEmpty -> {
                item {
                    EmptyStateView(
                        modifier = Modifier.fillParentMaxSize()
                    )
                }
            }
        }
    }
}

@Composable
fun <T : Any> LazyPagingItems<T>.rememberLazyListState2(): LazyListState {
    // After recreation, LazyPagingItems first return 0 items, then the cached items.
    // This behavior/issue is resetting the LazyListState scroll position.
    // Below is a workaround. More info<https://issuetracker.google.com/issues/177245496>.
    return when (itemCount) {
        // Return a different LazyListState instance.
        0 -> remember(this) { LazyListState(0, 0) }
        // Return rememberLazyListState (normal case).
        else -> rememberLazyListState()
    }
}
