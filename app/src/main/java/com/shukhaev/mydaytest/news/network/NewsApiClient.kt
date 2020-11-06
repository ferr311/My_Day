package com.shukhaev.mydaytest.news.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object NewsApiClient {

    const val BASE_URL = "https://api.currentsapi.services/"

    val newsApiClient: NewsApi by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        return@lazy retrofit.create(NewsApi::class.java)
    }

}