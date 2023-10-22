package com.borred.zimran_test_app.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun EmptyStateView(
    modifier: Modifier = Modifier
) {
    DisplayAndHeadline(
        display = ":(",
        headline = "Nothing found",
        modifier = modifier
    )
}
