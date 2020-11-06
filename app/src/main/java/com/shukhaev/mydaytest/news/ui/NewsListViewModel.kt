package com.shukhaev.mydaytest.news.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shukhaev.mydaytest.news.model.News
import com.shukhaev.mydaytest.news.repository.NewsRepository
import kotlinx.coroutines.launch

class NewsListViewModel : ViewModel() {

    private val repo = NewsRepository()

    private val listNewsLiveData: MutableLiveData<List<News>> = MutableLiveData()
    val listNews: LiveData<List<News>>
        get() = listNewsLiveData

    init {
        refreshNews()
    }

    fun refreshNews() {
        viewModelScope.launch {
            listNewsLiveData.postValue(repo.getNewsList())
        }
    }
}