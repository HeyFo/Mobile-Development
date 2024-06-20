package com.bangkit.heyfo.ui.detail.article

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bangkit.heyfo.data.retrofit.ApiService
import com.bangkit.heyfo.databinding.ActivityDetailArticleBinding
import com.bumptech.glide.Glide
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class DetailArticleActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailArticleBinding
    private lateinit var detailArticleViewModel: DetailArticleViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailArticleBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title= "Detail Artikel"

        // Inisialisasi ApiService
        val retrofit = Retrofit.Builder()
            .baseUrl("https://heyfo-6ppaqiiwua-et.a.run.app/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val apiService = retrofit.create(ApiService::class.java)

        // Inisialisasi ArticleRepository
        val repository = ArticleRepository(apiService)

        // Gunakan ViewModelFactory dengan ArticleRepository
        val factory = DetailArticleViewModelFactory(repository)
        detailArticleViewModel = ViewModelProvider(this, factory)[DetailArticleViewModel::class.java]

        // Retrieve the article UUID passed from the previous activity
        val articleUuid = intent.getStringExtra("article_uuid") ?: return

        // Observe the ViewModel to get article details
        detailArticleViewModel.getArticleDetail(articleUuid).observe(this) { article ->
            article?.let {
                binding.tvDetailArticleName.text = it.title
                binding.tvDetailArticleDesc.text = it.body
                binding.tvDetailArticleAuth.text = it.author

                Glide.with(this)
                    .load(it.imageUrl)
                    .into(binding.ivDetailArticle)
            }
        }
    }
}
