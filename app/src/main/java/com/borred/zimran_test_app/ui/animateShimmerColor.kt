package com.borred.zimran_test_app.ui

import androidx.compose.animation.animateColor
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.graphics.Color

@Composable
fun animateShimmerColor(
    initialValue: Color = Color.DarkGray,
    targetValue: Color = Color.White,
    animationDurationInMillis: Int = 1000
): State<Color> {
    val transition = rememberInfiniteTransition()
    return transition.animateColor(
        initialValue = initialValue,
        targetValue = targetValue,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = animationDurationInMillis,
                easing = LinearEasing
            ),
            repeatMode = RepeatMode.Reverse
        )
    )
}
