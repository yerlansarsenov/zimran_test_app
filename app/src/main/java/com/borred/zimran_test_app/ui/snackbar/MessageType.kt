package com.borred.zimran_test_app.ui.snackbar

import androidx.compose.ui.graphics.Color

enum class MessageType(val textColor: Color) {
    Success(textColor = Color.Green),
    Error(textColor = Color.Red),
    Info(textColor = Color.Gray)
}
