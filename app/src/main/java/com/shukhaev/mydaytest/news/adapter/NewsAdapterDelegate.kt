package com.shukhaev.mydaytest.news.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hannesdorfmann.adapterdelegates4.AbsListItemAdapterDelegate
import com.shukhaev.mydaytest.R
import com.shukhaev.mydaytest.news.model.News
import com.shukhaev.mydaytest.util.loadImage
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_list_news.*

class NewsAdapterDelegate(
    private val onItemClicked: (position: Int) -> Unit
) : AbsListItemAdapterDelegate<News, News, NewsAdapterDelegate.NewsHolder>() {


    override fun isForViewType(item: News, items: MutableList<News>, position: Int): Boolean {
        return item is News
    }

    override fun onCreateViewHolder(parent: ViewGroup): NewsHolder {
        return NewsHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_list_news_card, parent, false),
            onItemClicked
        )
    }

    override fun onBindViewHolder(item: News, holder: NewsHolder, payloads: MutableList<Any>) {
        holder.bind(item)
    }

    class NewsHolder(override val containerView: View, onItemClicked: (position: Int) -> Unit) :
        RecyclerView.ViewHolder(containerView),
        LayoutContainer {

        init {
            containerView.setOnClickListener { onItemClicked(adapterPosition) }
        }

        fun bind(news: News) {
            news_item_tv_title.text = news.title
            news_item_tv_description.text = news.description
            loadImage(news.image, news_item_iv_image)

        }

    }
}