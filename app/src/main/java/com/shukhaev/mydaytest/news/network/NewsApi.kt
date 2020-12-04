package com.shukhaev.mydaytest.news.network

import com.shukhaev.mydaytest.news.model.NewsResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApi {

    //cb5338dd82d54b91922e55aeb89401d4
    //kSZWKZ7Zwjv7T9_I5Cglq1QHWWx7jOCAHPEG8PFR2BJxIjDS

//    @GET("v1/latest-news")
    @GET("v2/top-headlines")
    suspend fun getLatestNews(
        @Query("apiKey") apiKey: String = "cb5338dd82d54b91922e55aeb89401d4",
        //@Query("language") language: String = "en"
        @Query("country") country: String = "ru"
    ): Response<NewsResponse>

}