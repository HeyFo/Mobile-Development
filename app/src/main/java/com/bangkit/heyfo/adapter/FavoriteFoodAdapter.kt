package com.bangkit.heyfo.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bangkit.heyfo.data.database.Food
import com.bangkit.heyfo.databinding.ItemFavoriteBinding
import com.bumptech.glide.Glide

class FavoriteFoodAdapter(
    private val onItemClick: (Food) -> Unit
) : ListAdapter<Food, FavoriteFoodAdapter.FavoriteViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteViewHolder {
        val binding = ItemFavoriteBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FavoriteViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {
        val food = getItem(position)
        if (food != null) {
            holder.bind(food)
        }
    }

    inner class FavoriteViewHolder(private val binding: ItemFavoriteBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(food: Food) {
            binding.apply {
                Glide.with(itemView.context)
                    .load(food.imageUrl) // Ganti dengan drawable yang sesuai
                    .into(itemFavorite)
                itemNameFavorite.text = food.name
                itemView.setOnClickListener { onItemClick(food) }
            }
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Food>() {
            override fun areItemsTheSame(oldItem: Food, newItem: Food): Boolean {
                return oldItem.name == newItem.name
            }

            @SuppressLint("DiffUtilEquals")
            override fun areContentsTheSame(oldItem: Food, newItem: Food): Boolean {
                return oldItem == newItem
            }
        }
    }
}
