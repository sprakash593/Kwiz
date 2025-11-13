package com.app.kwiz.data.repository

import android.content.Context
import com.app.kwiz.data.local.LocalDataSource
import com.app.kwiz.data.models.Question
import com.app.kwiz.data.service.ApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class KwizRepository(
    private val apiService: ApiService,
    private val localDataSource: LocalDataSource
) {

    suspend fun fetchQuestions(context: Context): List<Question> = withContext(Dispatchers.IO) {
        try {
            apiService.fetchQuestions()
        } catch (t: Throwable) {
            t.printStackTrace()
            loadLocalQuestions(context)
        }
    }

    private fun loadLocalQuestions(context: Context): List<Question> = localDataSource.loadQuestions(context)

}
