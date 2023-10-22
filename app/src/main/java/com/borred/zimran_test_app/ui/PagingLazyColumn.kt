package com.borred.zimran_test_app.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
