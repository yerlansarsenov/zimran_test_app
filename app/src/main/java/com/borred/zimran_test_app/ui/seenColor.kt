package com.borred.zimran_test_app.ui

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val seenColor: Color
    @Composable
    get() = MaterialTheme.colorScheme.secondary.copy(alpha = 0.2f)
