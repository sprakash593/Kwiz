package com.app.kwiz.ui.components

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.app.kwiz.ui.states.QuestionState
import com.app.kwiz.utils.CorrectGreen
import com.app.kwiz.utils.WrongRed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Cancel
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.Circle
import androidx.compose.material.icons.outlined.SkipNext

@Composable
fun <T> ItemListProgressBar(
    statuses: List<T>,
    currentIndex: Int,
    onItemClick: (Int) -> Unit
) {
    val listState = rememberLazyListState()

    // Auto-scroll to keep current question visible
    LaunchedEffect(currentIndex) {
        listState.animateScrollToItem(currentIndex)
    }

    LazyRow(
        state = listState,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(statuses.size) { index ->
            val status = statuses[index]

            // Choose icon + color
            val (icon, bgColor, iconTint) = when (status) {
                QuestionState.CORRECT -> Triple(
                    Icons.Outlined.CheckCircle,
                    MaterialTheme.colorScheme.primaryContainer,
                    CorrectGreen
                )
                QuestionState.WRONG -> Triple(
                    Icons.Outlined.Cancel,
                    MaterialTheme.colorScheme.errorContainer,
                    WrongRed
                )
                QuestionState.SKIPPED -> Triple(
                    Icons.Outlined.SkipNext,
                    MaterialTheme.colorScheme.secondaryContainer,
                    MaterialTheme.colorScheme.onSecondaryContainer
                )
                QuestionState.UNANSWERED -> Triple(
                    Icons.Outlined.Circle,
                    MaterialTheme.colorScheme.surfaceVariant,
                    MaterialTheme.colorScheme.onSurfaceVariant
                )

                else -> Triple(
                    Icons.Outlined.Circle,
                    MaterialTheme.colorScheme.surfaceVariant,
                    MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            // Highlight current question
            val borderColor = if (index == currentIndex)
                MaterialTheme.colorScheme.primary
            else
                Color.Transparent

            ElevatedCard(
                modifier = Modifier
                    .size(56.dp)
                    .border(
                        width = 3.dp, color = borderColor, shape = RoundedCornerShape(14.dp)
                    )
                    .clickable(enabled = status != QuestionState.UNANSWERED) {
                        onItemClick(index)
                    },
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.elevatedCardColors(containerColor = bgColor),
                elevation = CardDefaults.elevatedCardElevation(defaultElevation = 4.dp),
            ) {

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(10.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Q.${index + 1}",
                        style = MaterialTheme.typography.bodySmall,
                    )

                    Icon(
                        imageVector = icon,
                        contentDescription = "Q${index + 1}",
                        tint = iconTint
                    )
                }
            }
        }
    }
}