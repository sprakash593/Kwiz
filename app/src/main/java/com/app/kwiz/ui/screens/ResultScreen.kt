package com.app.kwiz.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.app.kwiz.ui.states.KwizUiState
import com.app.kwiz.ui.viewmodel.KwizViewModel
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.scaleIn
import androidx.compose.foundation.background
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.app.kwiz.ui.components.StatCard
import kotlinx.coroutines.delay


@Composable
fun ResultScreen(viewModel: KwizViewModel, onRestart: () -> Unit) {
    val state = viewModel.uiState
    if (state !is KwizUiState.Finished) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Loading results...")
        }
        return
    }

    // animation entrance
    val visible = remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        delay(200)
        visible.value = true
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 24.dp, vertical = 32.dp), contentAlignment = Alignment.Center
    ) {
        AnimatedVisibility(
            visible = visible.value, enter = fadeIn() + scaleIn()
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // 🏆 header emoji
                Text(
                    text = if (state.correctCount > state.questions.size / 2) "🏆 Great Job!" else "💪 Quiz Completed!",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontSize = 28.sp, fontWeight = FontWeight.Bold
                    ),
                    color = MaterialTheme.colorScheme.primary,
                    textAlign = TextAlign.Center
                )

                Spacer(Modifier.height(12.dp))

                Text(
                    text = "You answered ${state.correctCount} out of ${state.questions.size} correctly.",
                    style = MaterialTheme.typography.bodyLarge.copy(
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.8f)
                    ),
                    textAlign = TextAlign.Center
                )

                Spacer(Modifier.height(28.dp))

                // 📊 Stats cards row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    StatCard(
                        "✅ Correct",
                        state.correctCount.toString(),
                        MaterialTheme.colorScheme.primary
                    )
                    StatCard("⏭ Skipped", state.skippedCount.toString(), Color.Gray)
                    StatCard("🔥 Streak", state.longestStreak.toString(), Color(0xFFFFA726))
                }

                Spacer(Modifier.height(40.dp))

                // 🎯 Restart button
                Button(
                    onClick = onRestart,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                        .shadow(6.dp, RoundedCornerShape(14.dp)),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                    contentPadding = PaddingValues()
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                brush = Brush.horizontalGradient(
                                    listOf(
                                        MaterialTheme.colorScheme.primary,
                                        MaterialTheme.colorScheme.secondary
                                    )
                                ), shape = RoundedCornerShape(14.dp)
                            ), contentAlignment = Alignment.Center
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Refresh,
                                contentDescription = "Restart",
                                tint = Color.White
                            )
                            Spacer(Modifier.width(6.dp))
                            Text(
                                text = "Restart Quiz",
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp
                            )
                        }
                    }
                }
            }
        }
    }
}

