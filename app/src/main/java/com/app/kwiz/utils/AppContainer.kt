package com.app.kwiz.utils

import com.app.kwiz.data.local.LocalDataSource
import com.app.kwiz.data.network.NetworkModule
import com.app.kwiz.data.repository.KwizRepository
import com.app.kwiz.data.service.ApiService

object AppContainer {

    fun provideKwizRepository(): KwizRepository {
        val logging = NetworkModule.makeLoggingInterceptor(true)
        val okHttp = NetworkModule.makeOkHttpClient(logging)
        val retrofit = NetworkModule.makeRetrofit(okHttp)
        val api = retrofit.create(ApiService::class.java)
        val local = LocalDataSource()
        return KwizRepository(api, local)
    }
}