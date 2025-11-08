package com.app.kwiz.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import kotlin.random.Random

@Composable
fun ParticleBurst() {
    val particles = remember {
        List(20) {
            Particle(
                angle = Random.nextFloat() * 360f,
                distance = Random.nextFloat() * 200f + 50f,
                size = Random.nextFloat() * 8f + 3f,
                color = listOf(
                    Color(0xFF1976D2), // blue
                    Color(0xFFD32F2F), // red
                    Color(0xFF388E3C)  // green
                ).random()
            )
        }
    }

    val animProgress = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        animProgress.animateTo(
            targetValue = 1f,
            animationSpec = tween(1000, easing = LinearOutSlowInEasing)
        )
    }

    Canvas(modifier = Modifier.fillMaxSize()) {
        particles.forEach { p ->
            val radians = Math.toRadians(p.angle.toDouble())
            val x = size.width / 2 + (p.distance * animProgress.value * kotlin.math.cos(radians)).toFloat()
            val y = size.height / 2 + (p.distance * animProgress.value * kotlin.math.sin(radians)).toFloat()
            drawCircle(
                color = p.color.copy(alpha = 1f - animProgress.value),
                radius = p.size,
                center = Offset(x, y)
            )
        }
    }
}

private data class Particle(
    val angle: Float,
    val distance: Float,
    val size: Float,
    val color: Color,
)