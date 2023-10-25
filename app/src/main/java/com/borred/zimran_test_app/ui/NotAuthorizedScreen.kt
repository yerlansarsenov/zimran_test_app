package com.borred.zimran_test_app.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.BiasAlignment
import androidx.compose.ui.Modifier
import com.borred.zimran_test_app.search.RouteButton
import com.borred.zimran_test_app.search.RouteButtonType

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
        RouteButton(
            type = RouteButtonType.Primary,
            title = "Login",
            onClick = authorizeViaGithub,
            modifier = Modifier
                .align(BiasAlignment(horizontalBias = 0f, verticalBias = -0.3f))
        )
    }
}
