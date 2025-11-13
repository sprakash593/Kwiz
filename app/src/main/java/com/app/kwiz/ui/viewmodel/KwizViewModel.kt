package com.app.kwiz.ui.viewmodel

import android.content.Context
import android.util.Log
import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.kwiz.ui.states.QuestionState
import com.app.kwiz.data.repository.KwizRepository
import com.app.kwiz.ui.states.KwizUiState
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class KwizViewModel(
    private val repository: KwizRepository) : ViewModel() {

    var uiState: KwizUiState by mutableStateOf(KwizUiState.Loading)
        private set

    private var autoAdvanceJob: Job? = null

    fun load(context: Context) {
        viewModelScope.launch {
            uiState = KwizUiState.Loading
            try {
                val questions = repository.fetchQuestions(context.applicationContext)
                uiState = KwizUiState.Ready(questions = questions)
            } catch (t: Throwable) {
                t.printStackTrace()
                uiState = KwizUiState.Error(t.message ?: "Unknown error")
            }
        }
    }

    fun selectOption(index: Int) {
        val state = uiState
        if (state !is KwizUiState.Ready || state.revealed) return

        val question = state.questions[state.currentIndex]
        val isCorrect = index == question.answerIndex
        val newCorrectCount = if (isCorrect) state.correctCount + 1 else state.correctCount
        val newCurrentStreak = if (isCorrect) state.currentStreak + 1 else 0
        val newLongest = maxOf(state.longestStreak, newCurrentStreak)

        val updatedStatuses = state.statuses.toMutableList()
        updatedStatuses[state.currentIndex] = if (isCorrect) QuestionState.CORRECT else QuestionState.WRONG

        // update with revealed state
        uiState = state.copy(
            selectedOptionIndex = index,
            revealed = true,
            correctCount = newCorrectCount,
            currentStreak = newCurrentStreak,
            longestStreak = newLongest,
            statuses = updatedStatuses
        )

        scheduleAutoAdvance()
    }

    fun skip() {
        val state = uiState
        if (state !is KwizUiState.Ready) return

        val updatedStatuses = state.statuses.toMutableList()
        updatedStatuses[state.currentIndex] = QuestionState.SKIPPED

        // increment skipped count and advance
        val updated = state.copy(
            skippedCount = state.skippedCount + 1,
            selectedOptionIndex = null,
            revealed = false,
            currentStreak = 0, // skipping counts as not correct (or you could decide differently)
            statuses = updatedStatuses
        )
        moveNext(updated)
    }

    private fun scheduleAutoAdvance() {
        autoAdvanceJob?.cancel()
        autoAdvanceJob = viewModelScope.launch {
            delay(1000L) // 1 seconds
            advanceAfterReveal()
        }
    }

    private fun advanceAfterReveal() {
        val state = uiState
        if (state !is KwizUiState.Ready) return
        // clear selection and move next
        moveNext(state.copy(selectedOptionIndex = null, revealed = false))
    }

    private fun moveNext(state: KwizUiState.Ready) {
        val nextIndex = state.currentIndex + 1
        uiState = if (nextIndex >= state.questions.size) {
            // finish
            KwizUiState.Finished(
                questions = state.questions,
                correctCount = state.correctCount,
                skippedCount = state.skippedCount,
                longestStreak = state.longestStreak
            )
        } else {
            state.copy(
                currentIndex = nextIndex,
                selectedOptionIndex = null,
                revealed = false
            )
        }
    }

    fun restart() {
        val list = when (val state = uiState) {
            is KwizUiState.Ready -> state.questions
            is KwizUiState.Finished -> state.questions
            else -> emptyList()
        }
        uiState = KwizUiState.Ready(questions = list)
    }

    fun goToQuestion(index: Int) {
        val state = uiState
        if (state !is KwizUiState.Ready) return
        if (index < 0 || index >= state.questions.size) return
        if (state.statuses[index] == QuestionState.UNANSWERED) return

        uiState = state.copy(
            currentIndex = index,
            selectedOptionIndex = null,
            revealed = false
        )
    }


    override fun onCleared() {
        autoAdvanceJob?.cancel()
        super.onCleared()
    }
}
