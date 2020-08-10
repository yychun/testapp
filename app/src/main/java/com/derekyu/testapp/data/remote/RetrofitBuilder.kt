package com.derekyu.testapp.data.remote

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitBuilder {

    private const val BASE_URL = "https://itunes.apple.com/hk/"

    private fun getRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val APP_REMOTE_SERVICE: AppRemoteService = getRetrofit().create(AppRemoteService::class.java)
}