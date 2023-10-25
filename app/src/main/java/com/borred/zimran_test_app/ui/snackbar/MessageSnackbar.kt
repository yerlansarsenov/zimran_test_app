package com.borred.zimran_test_app.ui.snackbar

import android.content.res.Configuration
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FractionalThreshold
import androidx.compose.material.Text
import androidx.compose.material.rememberSwipeableState
import androidx.compose.material.swipeable
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.borred.zimran_test_app.ui.Shadow
import com.borred.zimran_test_app.ui.coloredShadow
import kotlinx.coroutines.delay
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

suspend fun SnackbarHostState.showSnackbarWithContent(
    messageContent: MessageContent
) {
    val json = Json.encodeToString(messageContent)
    showSnackbar(message = json)
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MessageSnackbar(
    snackbarHostState: SnackbarHostState,
    modifier: Modifier = Modifier
) {
    if (snackbarHostState.currentSnackbarData == null) {
        return
    }
    var size by remember { mutableStateOf(Size.Zero) }
    val swipeableState = rememberSwipeableState(SwipeDirection.Initial)
    val height = remember(size) {
        if (size.height == 0f) {
            1f
        } else {
            size.height
        }
    }
    if (swipeableState.isAnimationRunning) {
        DisposableEffect(Unit) {
            onDispose {
                when (swipeableState.currentValue) {
                    SwipeDirection.Up -> snackbarHostState.currentSnackbarData?.dismiss()
                    else -> return@onDispose
                }
            }
        }
    }
    val offset = with(LocalDensity.current) {
        swipeableState.offset.value.toDp()
    }
    MessageSnackbarHost(
        hostState = snackbarHostState,
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight(Alignment.Top)
            .onSizeChanged { size = Size(it.width.toFloat(), it.height.toFloat()) }
            .swipeable(
                state = swipeableState,
                anchors = mapOf(
                    -height to SwipeDirection.Up,
                    0f to SwipeDirection.Initial
                ),
                thresholds = { _, _ -> FractionalThreshold(0.2f) },
                orientation = Orientation.Vertical
            )
            .padding(start = 16.dp, top = 56.dp - 4.dp, end = 16.dp)
    ) { data ->
        val messageContent = remember(data) {
            Json.decodeFromString<MessageContent>(data.visuals.message)
        }
        val cornerRadius = remember { 12.dp }
        val cornerShape = remember { RoundedCornerShape(cornerRadius) }
        MessageSnackbarView(
            messageContent = messageContent,
            cornerRadius = cornerRadius,
            cornerShape = cornerShape,
            verticalOffset = offset
        )
    }
}

@Composable
private fun MessageSnackbarView(
    messageContent: MessageContent,
    cornerRadius: Dp,
    cornerShape: Shape,
    verticalOffset: Dp
) {
    val haptic = LocalHapticFeedback.current
    LaunchedEffect(messageContent.type) {
        when (messageContent.type) {
            MessageType.Info -> {
                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
            }
            MessageType.Success -> {
                repeat(2) {
                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                    delay(100)
                }
            }
            MessageType.Error -> {
                repeat(4) {
                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                    delay(100)
                }
            }
        }
    }

    Card(
        backgroundColor = MaterialTheme.colorScheme.background,
        shape = cornerShape,
        border = BorderStroke(
            width = 0.5.dp,
            color = Color.Gray
        ),
        modifier = Modifier
            .fillMaxWidth()
            .offset(y = verticalOffset)
            .coloredShadow(
                shadow = Shadow(
                    color = Color(0xFF111111),
                    offsetY = 17.dp,
                    alpha = 0.15F,
                    cornersRadius = cornerRadius,
                    shadowBlurRadius = 20.dp
                )
            )
    ) {
        Column {
            Text(
                modifier = Modifier.padding(start = 14.dp, top = 12.dp, end = 10.dp),
                text = messageContent.title,
                color = messageContent.type.textColor,
                fontSize = 16.sp,
                textAlign = TextAlign.Start
            )
            Text(
                modifier = Modifier.padding(
                    start = 14.dp,
                    top = 4.dp,
                    end = 18.dp,
                    bottom = 12.dp
                ),
                text = messageContent.subtitle,
                color = MaterialTheme.colorScheme.primary,
                fontSize = 14.sp,
                textAlign = TextAlign.Start
            )
        }
    }
}

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun MessageSnackbarViewPreview() {
    val cornerRadius = remember { 12.dp }
    val cornerShape = remember { RoundedCornerShape(cornerRadius) }
    val messageContent = remember {
        MessageContent(
            title = "Error",
            subtitle = "real error",
            type = MessageType.Error
        )
    }
    MessageSnackbarView(
        messageContent = messageContent,
        cornerRadius = cornerRadius,
        cornerShape = cornerShape,
        verticalOffset = 0.dp
    )
}
