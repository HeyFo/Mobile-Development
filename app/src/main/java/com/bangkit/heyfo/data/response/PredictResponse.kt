package com.bangkit.heyfo.data.response

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class PredictResponse(

	@field:SerializedName("data")
	val data: List<DataItemPredict?>? = null,

	@field:SerializedName("predict_result")
	val predictResult: List<String?>? = null
)

data class DataItemPredict(

	@field:SerializedName("image_url")
	val imageUrl: String? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("matched_ingredients")
	val matchedIngredients: List<String?>? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("uuid")
	val uuid: String? = null,

	@field:SerializedName("unmatched_ingredients")
	val unmatchedIngredients: List<String?>? = null
): Serializable
