package com.app.kwiz.ui.states

import com.app.kwiz.data.models.Question

sealed interface KwizUiState {
    object Loading : KwizUiState

    data class Error(val message: String) : KwizUiState

    data class Ready(
        val questions: List<Question>,
        val currentIndex: Int = 0,
        val selectedOptionIndex: Int? = null,
        val revealed: Boolean = false,
        val correctCount: Int = 0,
        val skippedCount: Int = 0,
        val currentStreak: Int = 0,
        val longestStreak: Int = 0,
        val statuses: List<QuestionState> = List(questions.size) { QuestionState.UNANSWERED }
    ) : KwizUiState

    data class Finished(
        val questions: List<Question>,
        val correctCount: Int,
        val skippedCount: Int,
        val longestStreak: Int
    ) : KwizUiState
}