package com.app.kwiz.data.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Question(
    @SerialName("id") val id: Int,
    @SerialName("question") val question: String,
    @SerialName("options") val options: List<String>,
    @SerialName("correctOptionIndex") val answerIndex: Int
)