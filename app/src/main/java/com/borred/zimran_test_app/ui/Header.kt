package com.borred.zimran_test_app.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp

@Composable
fun Header(
    title: String,
    modifier: Modifier = Modifier
) {
    HeaderImpl(
        title = title,
        actionButton = null,
        secondActionButton = null,
        modifier = modifier
    )
}

@Composable
fun Header(
    title: String,
    actionButtonIcon: ImageVector,
    onClickAction: () -> Unit,
    modifier: Modifier = Modifier
) {
    HeaderImpl(
        title = title,
        actionButton = ActionButton(
            icon = actionButtonIcon,
            onClick = onClickAction
        ),
        secondActionButton = null,
        modifier = modifier
    )
}

@Composable
fun Header(
    title: String,
    actionButtonIcon: ImageVector,
    onClickAction: () -> Unit,
    secondaryActionButtonIcon: ImageVector,
    onClickSecondaryAction: () -> Unit,
    modifier: Modifier = Modifier
) {
    HeaderImpl(
        title = title,
        actionButton = ActionButton(
            icon = actionButtonIcon,
            onClick = onClickAction
        ),
        secondActionButton = ActionButton(
            icon = secondaryActionButtonIcon,
            onClick = onClickSecondaryAction
        ),
        modifier = modifier
    )
}

private data class ActionButton(
    val icon: ImageVector,
    val onClick: () -> Unit
)

@Composable
private fun HeaderImpl(
    title: String,
    actionButton: ActionButton?,
    secondActionButton: ActionButton?,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
            .heightIn(min = 56.dp)
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.primary,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.weight(1f)
        )
        actionButton?.let {
            Icon(
                painter = rememberVectorPainter(image = it.icon),
                contentDescription = "action button",
                modifier = Modifier
                    .size(36.dp)
                    .clickable(onClick = it.onClick),
                tint = MaterialTheme.colorScheme.primary
            )
        }
        secondActionButton?.let {
            Spacer(modifier = Modifier.width(20.dp))
            Icon(
                painter = rememberVectorPainter(image = it.icon),
                contentDescription = "action button",
                modifier = Modifier
                    .size(36.dp)
                    .clickable(onClick = it.onClick),
                tint = MaterialTheme.colorScheme.primary
            )
        }
    }
}
