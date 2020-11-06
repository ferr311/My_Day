package com.shukhaev.mydaytest.news.model

import com.google.gson.annotations.SerializedName

data class News(
    @SerializedName("id")
    val id: String,
    @SerializedName("title")
    val title: String,
    @SerializedName("description")
    val description: String,
    @SerializedName("url")
    val url: String,
    @SerializedName("image")
    val image: String
)