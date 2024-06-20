package com.bangkit.heyfo.ui.detail.article

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bangkit.heyfo.data.response.DataArticle
import kotlinx.coroutines.launch

class DetailArticleViewModel(private val repository: ArticleRepository) : ViewModel() {

    private val _articleDetail = MutableLiveData<DataArticle?>()
    val articleDetail: LiveData<DataArticle?> get() = _articleDetail

    fun getArticleDetail(articleUuid: String): LiveData<DataArticle?> {
        viewModelScope.launch {
            val response = repository.getArticleDetail(articleUuid)
            _articleDetail.value = response?.data
        }
        return articleDetail
    }
}
