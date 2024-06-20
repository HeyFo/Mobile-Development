package com.bangkit.heyfo.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bangkit.heyfo.data.response.DataItemPredict
import com.bangkit.heyfo.databinding.ListItemPredictBinding
import com.bumptech.glide.Glide

class PredictAdapter(
    private val predictList: List<DataItemPredict>,
    private val clickListener: (String) -> Unit
) : RecyclerView.Adapter<PredictAdapter.PredictViewHolder>() {

    inner class PredictViewHolder(private val binding: ListItemPredictBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(data: DataItemPredict) {
            // Set item name
            binding.itemName.text = data.name ?: "Unknown"

            // Load image using Glide
            data.imageUrl?.let { imageUrl ->
                Glide.with(binding.itemImage.context)
                    .load(imageUrl)
                    .into(binding.itemImage)
            }

            // Bind matched ingredients to itemMatch
            val matchedIngredientsText = data.matchedIngredients
                ?.filterNotNull()
                ?.joinToString(separator = ", ") ?: "No matched ingredients"
            binding.itemMatch.text = "$matchedIngredientsText"

            // Bind unmatched ingredients to itemUnmatch
            val unmatchedIngredientsText = data.unmatchedIngredients
                ?.filterNotNull()
                ?.joinToString(separator = ", ") ?: "No unmatched ingredients"
            binding.itemUnmatch.text = "$unmatchedIngredientsText"

            // Set click listener on the root view
            binding.root.setOnClickListener {
                data.uuid?.let { uuid ->
                    clickListener(uuid)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PredictViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ListItemPredictBinding.inflate(inflater, parent, false)
        return PredictViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PredictViewHolder, position: Int) {
        holder.bind(predictList[position])
    }

    override fun getItemCount(): Int = predictList.size
}
