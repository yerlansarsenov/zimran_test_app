package com.borred.zimran_test_app.ui.snackbar

import kotlinx.serialization.Serializable

@Serializable
class MessageContent(
    val title: String,
    val subtitle: String,
    val type: MessageType
)
