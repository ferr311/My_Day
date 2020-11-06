package com.shukhaev.mydaytest.news.ui

import android.os.Bundle
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.shukhaev.mydaytest.R
import kotlinx.android.synthetic.main.fragment_news_detail.*

class NewsDetailFragment : Fragment(R.layout.fragment_news_detail) {

    private val args: NewsDetailFragmentArgs by navArgs()

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)


        news_detail_webView.apply {
            loadUrl(args.url)
            settings.javaScriptEnabled = true
            webViewClient = WebViewClient()
        }
    }

}