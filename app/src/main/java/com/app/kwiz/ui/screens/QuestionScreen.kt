package com.app.kwiz.ui.screens

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.RestartAlt
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.app.kwiz.data.models.Question
import com.app.kwiz.ui.components.ItemListProgressBar
import com.app.kwiz.ui.components.StreakBadge
import com.app.kwiz.ui.components.TimerAnimation
import com.app.kwiz.ui.states.KwizUiState
import com.app.kwiz.utils.CorrectGreen
import com.app.kwiz.utils.WrongRed
import com.app.kwiz.ui.viewmodel.KwizViewModel


@Composable
fun QuestionScreen(
    viewModel: KwizViewModel,
    onFinish: () -> Unit,
    onRestart: () -> Unit
) {
    val state = viewModel.uiState
    QuestionScreenContent(
        state = state,
        onSkip = { viewModel.skip() },
        onSelectOption = { viewModel.selectOption(it) },
        onFinish = onFinish,
        onRestart = onRestart,
        goToQuestion = { viewModel.goToQuestion(it) }
    )
}

@Composable
fun QuestionScreenContent(
    state: KwizUiState,
    onSkip: () -> Unit,
    onSelectOption: (Int) -> Unit,
    onFinish: () -> Unit,
    onRestart: () -> Unit,
    goToQuestion: (Int) -> Unit) {
    when (state) {
        is KwizUiState.Loading -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text("Loading...")
            }
        }

        is KwizUiState.Error -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text("Error: ${state.message}")
            }
        }

        is KwizUiState.Ready -> {
            if (state.currentIndex >= state.questions.size) {
                onFinish()
                return
            }

            val question = state.questions[state.currentIndex]

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {

                // --- Top Row: Progress, streak badge, skip ---

                Spacer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(32.dp)
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TimerAnimation(
                        key = (state.currentIndex + 1).toFloat() / state.questions.size,
                        durationMillis = 20 * 1000, // time per question
                        onTimerComplete = { onSkip() })

                    StreakBadge(
                        currentStreak = state.currentStreak,
                        longest = state.longestStreak
                    )

                    Icon(
                        modifier = Modifier
                            .clickable { onRestart() }
                            .padding(8.dp),
                        imageVector = Icons.Filled.RestartAlt,
                        contentDescription = "Undo",
                    )
                }
                Spacer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(32.dp)
                )
                // --- Question Progress bar ---
                ItemListProgressBar(
                    statuses = state.statuses,
                    currentIndex = state.currentIndex,
                    onItemClick = { goToQuestion(it) }
                )

                // --- Question text ---
                Text(
                    text =  "Q.${question.id} ${question.question}",
                    style = MaterialTheme.typography.titleMedium,
                    minLines = 3
                )

                // --- Options list ---
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    question.options.forEachIndexed { index, option ->
                        val isSelected = state.selectedOptionIndex == index
                        val revealed = state.revealed
                        val isCorrect = index == question.answerIndex

                        val targetColor by animateColorAsState(
                            targetValue = when {
                                !revealed && isSelected -> MaterialTheme.colorScheme.secondaryContainer
                                revealed && isCorrect -> CorrectGreen
                                revealed && isSelected && !isCorrect -> WrongRed
                                else -> MaterialTheme.colorScheme.surface
                            }
                        )

                        ElevatedCard(
                            modifier = Modifier
                                .fillMaxWidth()
                                .heightIn(min = 56.dp)
                                .pointerInput(Unit) {
                                    detectTapGestures {
                                        if (!state.revealed) onSelectOption(index)
                                    }
                                },
                            colors = CardDefaults.elevatedCardColors(containerColor = targetColor)
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(horizontal = 12.dp, vertical = 8.dp),
                                contentAlignment = Alignment.CenterStart
                            ) {
                                Text(
                                    text = option,
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                        }
                    }
                }

                Spacer(Modifier.weight(1f))
                
                Button(
                    onClick = onSkip,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 8.dp)
                ) {
                    Text(text = "Skip", style = MaterialTheme.typography.bodyLarge)
                }
            }
        }

        is KwizUiState.Finished -> onFinish()
    }
}


@Preview(showBackground = true)
@Composable
fun PreviewQuestionScreenContent() {
    val sampleState = KwizUiState.Ready(
        questions = listOf(
            Question(
                id = 1,
                question = "What is the capital of France?",
                options = listOf("Berlin", "Madrid", "Paris", "Rome"),
                answerIndex = 2
            )
        ),
        currentIndex = 0,
        selectedOptionIndex = 1,
        revealed = true,
        currentStreak = 4,
        longestStreak = 7
    )
    QuestionScreenContent(
        state = sampleState,
        onSkip = {},
        onSelectOption = {},
        onFinish = {},
        onRestart = {},
        goToQuestion = {}
    )
}





