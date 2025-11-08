package com.app.kwiz.data.service

import retrofit2.http.GET


interface ApiService {

    @GET("questions")
    suspend fun fetchQuestions(): String
}