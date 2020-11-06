package com.shukhaev.mydaytest.news.adapter

import androidx.recyclerview.widget.DiffUtil
import com.hannesdorfmann.adapterdelegates4.AsyncListDifferDelegationAdapter
import com.shukhaev.mydaytest.news.model.News

class NewsAdapter(onItemClicked: (position: Int) -> Unit) :
    AsyncListDifferDelegationAdapter<News>(NewsDiffUtil()) {

    init {
        delegatesManager.addDelegate(NewsAdapterDelegate(onItemClicked))
    }

    class NewsDiffUtil : DiffUtil.ItemCallback<News>() {
        override fun areItemsTheSame(oldItem: News, newItem: News): Boolean {
            return when {
                oldItem is News && newItem is News -> oldItem.id == newItem.id

                else -> false
            }
        }

        override fun areContentsTheSame(oldItem: News, newItem: News): Boolean {
            return newItem == oldItem
        }

    }
}