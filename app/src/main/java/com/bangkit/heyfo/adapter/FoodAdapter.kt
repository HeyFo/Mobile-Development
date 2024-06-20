package com.bangkit.heyfo.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bangkit.heyfo.R
import com.bangkit.heyfo.data.response.DataItem
import com.bumptech.glide.Glide

class FoodAdapter(
    private var list: List<DataItem?>,
    private val onItemClickListener: (DataItem) -> Unit
) : RecyclerView.Adapter<FoodAdapter.FoodViewHolder>() {

    @SuppressLint("NotifyDataSetChanged")
    fun updateData(newList: List<DataItem?>) {
        list = newList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)
        return FoodViewHolder(view)
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: FoodViewHolder, position: Int) {
        list[position]?.let { holder.bind(it) }
    }

    inner class FoodViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(dataItem: DataItem) {
            itemView.apply {
                findViewById<TextView>(R.id.tvNameList).text = dataItem.name
                val imageView = findViewById<ImageView>(R.id.IvList)
                Glide.with(context).load(dataItem.imageUrl).into(imageView)

                setOnClickListener {
                    onItemClickListener(dataItem)
                }
            }
        }
    }
}
