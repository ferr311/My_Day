package com.shukhaev.mydaytest.news.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.shukhaev.mydaytest.R
import com.shukhaev.mydaytest.news.adapter.NewsAdapter
import jp.wasabeef.recyclerview.animators.FlipInTopXAnimator
import kotlinx.android.synthetic.main.fragment_news.*

class NewsListFragment : Fragment(R.layout.fragment_news) {

    private val newsListViewModel: NewsListViewModel by viewModels(ownerProducer = { requireParentFragment() })
    private var newsAdapter: NewsAdapter? =
        NewsAdapter { position -> navigateToNewsDetail(position) }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        initRecyclerView()
        news_fab_refresh.setOnClickListener { refreshNews() }

        newsListViewModel.listNews.observe(viewLifecycleOwner) {
            newsAdapter?.items = it
            news_recycler_view.smoothScrollToPosition(0)
        }
    }

    private fun refreshNews() {
        newsListViewModel.refreshNews()

    }

    private fun initRecyclerView() {
        with(news_recycler_view) {
            layoutManager = LinearLayoutManager(requireContext())
            itemAnimator = FlipInTopXAnimator()
            adapter = newsAdapter
        }
    }

    private fun navigateToNewsDetail(position: Int) {
        val imageUrl = newsAdapter?.items?.get(position)?.image
        val content = newsAdapter?.items?.get(position)?.content

        val action =
            imageUrl?.let { content?.let { it1 ->
                NewsListFragmentDirections.actionNavigationNewsToFragmentNewsDetail(it,
                    it1
                )
            } }
        action?.let { findNavController().navigate(it) }

    }

    override fun onDestroy() {
        super.onDestroy()
        newsAdapter = null
    }

}