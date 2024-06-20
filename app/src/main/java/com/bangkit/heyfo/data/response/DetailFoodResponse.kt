package com.bangkit.heyfo.data.response

import com.google.gson.annotations.SerializedName

data class DetailFoodResponse(

	@field:SerializedName("data")
	val data: Data? = null
)

data class Data(

	@field:SerializedName("image_url")
	val imageUrl: String? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("ingredients")
	val ingredients: List<String?>? = null,

	@field:SerializedName("cooking_step")
	val cookingStep: List<String?>? = null,

	@field:SerializedName("uuid")
	val uuid: String? = null
)
