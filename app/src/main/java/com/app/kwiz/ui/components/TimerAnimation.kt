package com.app.kwiz.ui.components

import androidx.compose.animation.core.animate
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.unit.dp

@Composable
fun TimerAnimation(progress: Float, durationMillis: Int, onTimerComplete: (() -> Unit)? = null) {
    var animatedProgress by remember { mutableStateOf(1f) }
    LaunchedEffect(progress) {
        animatedProgress = 1f
        animate(
            initialValue = 1f,
            targetValue = 0f,
            animationSpec = tween(durationMillis)
        ) { value, _ ->
            animatedProgress = value
            if (value == 0f) {
                onTimerComplete?.invoke()
            }
        }
    }
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.size(40.dp)
    ) {
        // Background circle
        Canvas(modifier = Modifier.size(40.dp)) {
            drawCircle(
                color = Color.White,
                radius = size.minDimension / 2
            )
            // Foreground arc (timer)
            drawArc(
                color = lerp(Color.Cyan, Color.Red, 1f - animatedProgress),
                startAngle = -90f,
                sweepAngle = 360f * animatedProgress,
                useCenter = false,
                style = Stroke(width = 6.dp.toPx())
            )
        }
        // Optional: show seconds left in the center
        val secondsLeft = (animatedProgress * (durationMillis / 1000)).toInt()
        Text(
            text = "$secondsLeft",
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}