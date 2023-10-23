package com.borred.zimran_test_app.ui.actiondialog

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.borred.zimran_test_app.ui.Shadow
import com.borred.zimran_test_app.ui.coloredShadows
import kotlinx.collections.immutable.ImmutableList

@Composable
fun ActionDialog(
    title: String,
    actionItems: ImmutableList<ActionItem>,
    onDismiss: () -> Unit
) {
    Dialog(
        onDismissRequest = { onDismiss() },
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        ActionSheet(
            title = title,
            actionItems = actionItems,
            onDismiss = onDismiss
        )
    }
}

@Composable
fun ActionDialog(
    actionItems: ImmutableList<ActionItem>,
    onDismiss: () -> Unit
) {
    Dialog(
        onDismissRequest = { onDismiss() },
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        ActionSheet(
            title = null,
            actionItems = actionItems,
            onDismiss = onDismiss
        )
    }
}

@Composable
private fun ActionSheet(
    title: String?,
    actionItems: ImmutableList<ActionItem>,
    onDismiss: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .clickable(
                indication = null,
                onClick = { onDismiss() },
                interactionSource = remember { MutableInteractionSource() }
            )
    ) {
        Column(
            modifier = Modifier
                .clip(RoundedCornerShape(12.dp))
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(start = 16.dp, end = 16.dp, bottom = 24.dp, top = 24.dp)
                .coloredShadows(
                    Shadow(alpha = 0.16F, offsetY = 8.dp, cornersRadius = 12.dp),
                    Shadow(alpha = 0.08F, cornersRadius = 12.dp)
                )
        ) {
            if (title != null) {
                Text(
                    modifier = Modifier.padding(start = 16.dp, top = 16.dp, end = 16.dp),
                    text = title,
                    color = Color.DarkGray,
                    fontSize = 14.sp
                )
                Spacer(modifier = Modifier.size(6.dp))
            }
            for (item in actionItems) {
                ActionSheetOption(
                    modifier = Modifier
                        .fillMaxWidth(),
                    actionText = item.text,
                    chosen = item.chosen,
                    onActionClicked = {
                        item.action.invoke()
                        onDismiss()
                    },
                    actionTextColor = item.actionTextColor
                )
            }
            TextButton(
                onClick = onDismiss,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text(
                    text = "Cancel",
                    color = Color(0xFFA71010)
                )
            }
        }
    }
}

@Composable
private fun ActionSheetOption(
    actionText: String,
    actionTextColor: Color,
    chosen: Boolean?,
    modifier: Modifier = Modifier,
    onActionClicked: () -> Unit
) {
    MaterialTheme {
        Row(
            modifier = modifier.clickable { onActionClicked() },
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                modifier = Modifier
                    .padding(start = 20.dp, top = 14.dp, bottom = 14.dp)
                    .weight(1F),
                text = actionText,
                color = actionTextColor,
                fontSize = 16.sp,
                textAlign = TextAlign.Start
            )

            if (chosen != null && chosen) {
                Icon(
                    painter = rememberVectorPainter(image = Icons.Default.Check),
                    contentDescription = null,
                    modifier = Modifier
                        .padding(end = 16.dp)
                        .size(20.dp),
                    tint = actionTextColor
                )
            }
        }
    }
}
