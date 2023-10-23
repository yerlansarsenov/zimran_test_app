package com.borred.zimran_test_app.ui

import androidx.compose.runtime.Immutable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Immutable
data class Shadow(
    val color: Color = Color.Black,
    val offsetY: Dp = 0.dp,
    val offsetX: Dp = 0.dp,
    val alpha: Float = 0.2F,
    val cornersRadius: Dp = 2.dp,
    val shadowBlurRadius: Dp = 8.dp
)

fun Modifier.coloredShadows(
    vararg shadows: Shadow
): Modifier {
    var modifier = this
    shadows.forEach {
        modifier = modifier.coloredShadow(it)
    }
    return modifier
}

fun Modifier.coloredShadow(
    shadow: Shadow
): Modifier = this.drawBehind {
    val shadowColor = shadow.color.copy(alpha = shadow.alpha).toArgb()
    val transparentColor = shadow.color.copy(alpha = 0f).toArgb()

    drawIntoCanvas {
        val paint = Paint()
        val frameworkPaint = paint.asFrameworkPaint()
        frameworkPaint.color = transparentColor
        frameworkPaint.setShadowLayer(
            shadow.shadowBlurRadius.toPx(),
            shadow.offsetX.toPx(),
            shadow.offsetY.toPx(),
            shadowColor
        )
        it.drawRoundRect(
            left = 0F,
            top = 0F,
            right = this.size.width,
            bottom = this.size.height,
            radiusX = shadow.cornersRadius.toPx(),
            radiusY = shadow.cornersRadius.toPx(),
            paint = paint
        )
    }
}

fun Modifier.coloredCircleShadow(
    shadow: Shadow
): Modifier = this.drawBehind {
    val shadowColor = shadow.color.copy(alpha = shadow.alpha).toArgb()
    val transparent = shadow.color.copy(alpha = 0f).toArgb()

    this.drawIntoCanvas {
        val paint = Paint()
        val frameworkPaint = paint.asFrameworkPaint()
        frameworkPaint.color = transparent

        frameworkPaint.setShadowLayer(
            shadow.shadowBlurRadius.toPx(),
            shadow.offsetX.toPx(),
            shadow.offsetY.toPx(),
            shadowColor
        )
        it.drawArc(
            left = 0f,
            top = 0f,
            right = this.size.width,
            bottom = this.size.height,
            paint = paint,
            startAngle = 0f,
            sweepAngle = 360f,
            useCenter = true
        )
    }
}
