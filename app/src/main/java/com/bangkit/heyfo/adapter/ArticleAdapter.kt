package com.bangkit.heyfo.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bangkit.heyfo.data.response.DataItemArticle
import com.bangkit.heyfo.databinding.ItemArticleBinding
import com.bumptech.glide.Glide

class ArticleAdapter(
    private var articles: List<DataItemArticle>?,
    private val onItemClick: (DataItemArticle) -> Unit
) : RecyclerView.Adapter<ArticleAdapter.ArticleViewHolder>() {

    fun updateData(newList: List<DataItemArticle>?) {
        articles = newList ?: emptyList()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
        val binding = ItemArticleBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ArticleViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
        articles?.get(position)?.let { holder.bind(it) }
    }

    override fun getItemCount(): Int {
        return articles?.size ?: 0
    }

    inner class ArticleViewHolder(private val binding: ItemArticleBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(article: DataItemArticle) {
            binding.tvTitle.text = article.title
            binding.tvAuthor.text = article.author

            // Load image using Glide or any other image loading library
            Glide.with(binding.root)
                .load(article.image)
                .into(binding.ivArticleImage)

            // Set click listener
            binding.root.setOnClickListener {
                onItemClick(article)
            }
        }
    }
}
