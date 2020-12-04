package com.shukhaev.mydaytest.news.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object NewsApiClient {

    const val BASE_URL = "https://api.currentsapi.services/"
    const val BASE_URL_temp = "http://newsapi.org/"

    val newsApiClient: NewsApi by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL_temp)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        return@lazy retrofit.create(NewsApi::class.java)
    }

}