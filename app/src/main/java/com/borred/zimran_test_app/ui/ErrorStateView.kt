package com.borred.zimran_test_app.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun ErrorStateView(
    modifier: Modifier = Modifier
) {
    DisplayAndHeadline(
        display = "Oh, no..",
        headline = "Something gone wrong",
        modifier = modifier
    )
}
