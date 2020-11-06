package com.shukhaev.mydaytest.news.model

import com.google.gson.annotations.SerializedName

data class NewsResponse (
     @SerializedName("news")
     val newsList:List<News>
 )