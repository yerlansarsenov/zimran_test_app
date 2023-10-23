package com.borred.zimran_test_app.ui.actiondialog

import androidx.compose.ui.graphics.Color

data class ActionItem(
    val text: String,
    val chosen: Boolean?,
    val actionTextColor: Color,
    val action: () -> Unit
)
