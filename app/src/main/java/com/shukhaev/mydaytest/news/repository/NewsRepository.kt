package com.shukhaev.mydaytest.news.repository

import com.shukhaev.mydaytest.news.model.News
import com.shukhaev.mydaytest.news.network.NewsApiClient

class NewsRepository {

    suspend fun getNewsList(): List<News>? {
        val res = NewsApiClient.newsApiClient.getLatestNews()

            return res.body()?.newsList

    }
}