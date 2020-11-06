package com.shukhaev.mydaytest.news.network

import com.shukhaev.mydaytest.news.model.NewsResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApi {

    @GET("v1/latest-news")
    suspend fun getLatestNews(
        @Query("apiKey") apiKey: String = "kSZWKZ7Zwjv7T9_I5Cglq1QHWWx7jOCAHPEG8PFR2BJxIjDS",
        @Query("language") language: String = "en"
    ): Response<NewsResponse>

}