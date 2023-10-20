package com.borred.zimran_test_app.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.BiasAlignment
import androidx.compose.ui.Modifier

@Composable
fun NotAuthorizedView(
    authorizeViaGithub: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .background(MaterialTheme.colorScheme.background)
            .fillMaxSize()
    ) {
        Button(
            modifier = Modifier
                .align(
                    BiasAlignment(horizontalBias = 0f, verticalBias = -0.3f)
                ),
            onClick = authorizeViaGithub,
            content = {
                Text(text = "Login")
            }
        )
    }
}
