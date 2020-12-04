package com.shukhaev.mydaytest.news.ui

import android.os.Bundle
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.shukhaev.mydaytest.R
import com.shukhaev.mydaytest.util.loadImage
import kotlinx.android.synthetic.main.fragment_news_detail.*

class NewsDetailFragment : Fragment(R.layout.fragment_news_detail) {

    private val args: NewsDetailFragmentArgs by navArgs()

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        loadImage(args.imageUrl, news_detail_iv)
        news_detail_tv.text = args.content
    }
}