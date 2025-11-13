package com.app.kwiz.data.local

import android.content.Context
import com.app.kwiz.data.models.Question
import kotlinx.serialization.json.Json

class LocalDataSource(
    private val json: Json = Json { ignoreUnknownKeys = true }
) {
    fun loadQuestions(context: Context): List<Question> {
        val jsonString = context.assets.open("questions.json")
            .bufferedReader()
            .use { it.readText() }

        return json.decodeFromString(jsonString)
    }
}