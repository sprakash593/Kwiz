package com.app.kwiz.data.repository

import android.content.Context
import com.app.kwiz.data.local.LocalDataSource
import com.app.kwiz.data.models.Question
import com.app.kwiz.data.service.ApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json

class KwizRepository(
    private val apiService: ApiService,
    private val localDataSource: LocalDataSource,
    private val json: Json = Json { ignoreUnknownKeys = true }
) {

    suspend fun fetchQuestions(): List<Question> = withContext(Dispatchers.IO) {
        try {
            val response = apiService.fetchQuestions()
            json.decodeFromString(response)
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun loadLocalQuestions(context: Context): List<Question> =
        localDataSource.loadQuestions(context)
}
