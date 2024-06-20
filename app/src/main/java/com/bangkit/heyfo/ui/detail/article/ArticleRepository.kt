package com.bangkit.heyfo.ui.detail.article

import com.bangkit.heyfo.data.response.DetailArticleResponse
import com.bangkit.heyfo.data.retrofit.ApiService

class ArticleRepository(private val apiService: ApiService) {

    suspend fun getArticleDetail(articleUuid: String): DetailArticleResponse? {
        return try {
            val response = apiService.getArticleDetail(articleUuid)
            if (response.isSuccessful) {
                response.body()
            } else {
                null
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}