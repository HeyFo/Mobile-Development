package com.bangkit.heyfo.data.retrofit

import com.bangkit.heyfo.data.response.AllFoodResponse
import com.bangkit.heyfo.data.response.ArticleResponse
import com.bangkit.heyfo.data.response.DetailArticleResponse
import com.bangkit.heyfo.data.response.DetailFoodResponse
import com.bangkit.heyfo.data.response.PredictResponse
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

interface ApiService {
    @GET("/api/foods")
    fun getAllFoods(
    ): Call<AllFoodResponse>

    @GET("/api/foods/{uuid}")
    fun getDetailFood(
        @Path("uuid") uuid: String
    ): Call<DetailFoodResponse>

    @Multipart
    @POST("/api/predict")
    suspend fun getPredict(
        @Part image: MultipartBody.Part
    ): Response<PredictResponse>

    @GET("/api/articles")
    fun getAllArticles(
    ): Call<ArticleResponse>

    @GET("/api/articles/{uuid}")
    suspend fun getArticleDetail(
        @Path("uuid") uuid: String
    ): Response<DetailArticleResponse>
}