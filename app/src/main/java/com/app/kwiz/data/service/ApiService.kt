package com.app.kwiz.data.service

import com.app.kwiz.data.models.Question
import retrofit2.http.GET


interface ApiService {

    @GET("dr-samrat/53846277a8fcb034e482906ccc0d12b2/raw")
    suspend fun fetchQuestions(): List<Question>
}