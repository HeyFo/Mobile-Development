package com.bangkit.heyfo.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bangkit.heyfo.data.response.AllFoodResponse
import com.bangkit.heyfo.data.response.ArticleResponse
import com.bangkit.heyfo.data.response.DataItem
import com.bangkit.heyfo.data.response.DataItemArticle
import com.bangkit.heyfo.data.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeViewModel : ViewModel() {

    private val _foodList = MutableLiveData<List<DataItem>>()
    val foodList: LiveData<List<DataItem>> = _foodList

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    private val _articleList = MutableLiveData<List<DataItemArticle>?>()
    val articleList: LiveData<List<DataItemArticle>?> = _articleList

    fun fetchData() {
        _isLoading.value = true
        val apiService = ApiConfig.getApiService()
        apiService.getAllFoods().enqueue(object : Callback<AllFoodResponse> {
            override fun onResponse(
                call: Call<AllFoodResponse>,
                response: Response<AllFoodResponse>
            ) {
                if (response.isSuccessful) {
                    val foodList = response.body()?.data?.filterNotNull() ?: emptyList()
                    val limitedList = if (foodList.size >= 5) {
                        foodList.subList(0, 5)
                    } else {
                        foodList
                    }
                    _foodList.value = limitedList
                } else {
                    _error.value = "Failed to fetch data"
                }
                _isLoading.value = false
            }

            override fun onFailure(call: Call<AllFoodResponse>, t: Throwable) {
                _error.value = "Failure fetching data"
                _isLoading.value = false
            }
        })
    }

    fun clearError() {
        _error.value = null
    }

    fun fetchArticleData() {
        _isLoading.value = true
        val apiService = ApiConfig.getApiService()
        apiService.getAllArticles().enqueue(object : Callback<ArticleResponse> {
            override fun onResponse(
                call: Call<ArticleResponse>,
                response: Response<ArticleResponse>
            ) {
                if (response.isSuccessful) {
                    val articles = response.body()?.data?.filterNotNull() ?: emptyList()
                    _articleList.value = articles
                } else {
                    _error.value = "Failed to fetch articles"
                }
                _isLoading.value = false
            }

            override fun onFailure(call: Call<ArticleResponse>, t: Throwable) {
                _error.value = "Failure fetching articles"
                _isLoading.value = false
            }
        })
    }
}



