package com.borred.zimran_test_app.search

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp

enum class RouteButtonType {
    Primary,
    Secondary
}

@Composable
fun RouteButton(
    type: RouteButtonType,
    title: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .padding(16.dp)
            .padding(horizontal = 16.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .heightIn(min = 44.dp)
            .composed {
                when (type) {
                    RouteButtonType.Primary -> {
                        this.background(MaterialTheme.colorScheme.primary)
                    }

                    RouteButtonType.Secondary -> {
                        this.border(
                            width = 4.dp,
                            color = MaterialTheme.colorScheme.primary,
                            shape = RoundedCornerShape(20.dp)
                        )
                    }
                }
            }
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge,
            color = when (type) {
                RouteButtonType.Primary -> MaterialTheme.colorScheme.background
                RouteButtonType.Secondary -> MaterialTheme.colorScheme.primary
            }
        )
    }
}
