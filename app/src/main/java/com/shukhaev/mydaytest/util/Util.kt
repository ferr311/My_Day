package com.shukhaev.mydaytest.util

import android.app.Activity
import android.widget.ImageView
import android.widget.Toast
import com.bumptech.glide.Glide

lateinit var APP_ACTIVITY: Activity

val <T> T.exhaustive:T
    get() = this

fun loadImage(url: String?, view: ImageView) {
    Glide.with(APP_ACTIVITY)
        .load(url)
        .into(view)
}

fun toast(text: String) {
    Toast.makeText(APP_ACTIVITY, text, Toast.LENGTH_SHORT).show()
}