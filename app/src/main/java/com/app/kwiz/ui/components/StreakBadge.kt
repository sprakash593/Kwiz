package com.app.kwiz.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.app.kwiz.utils.StreakYellow

@Composable
fun StreakBadge(currentStreak: Int, longest: Int) {
    val active = currentStreak >= 3
    val bg = if (active) StreakYellow else MaterialTheme.colorScheme.surfaceVariant

    Surface(
        shape = RoundedCornerShape(12.dp),
        color = bg,
        tonalElevation = if (active) 4.dp else 0.dp
    ) {
        Text(
            text = "🔥 Streak: $currentStreak (Best: $longest)",
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
            style = MaterialTheme.typography.labelMedium
        )
    }
}