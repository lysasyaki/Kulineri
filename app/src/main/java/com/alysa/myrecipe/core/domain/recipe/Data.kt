package com.alysa.myrecipe.core.domain.recipe

import com.google.gson.annotations.SerializedName
import io.realm.RealmList
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import java.io.Serializable

open class Data: RealmObject(), Serializable {
	@PrimaryKey
	var id: Int = 0

	@field:SerializedName("image")
	var image: RealmList<String>? = null

	@field:SerializedName("createdAt")
	var createdAt: String? = null

	@field:SerializedName("category_id")
	var categoryId: Int? = null

	@field:SerializedName("user_id")
	var userId: Int? = null

	@field:SerializedName("Ingredient")
	var ingredient: String? = null

	@field:SerializedName("name")
	var name: String? = null

	@field:SerializedName("description")
	var description: String? = null

	@field:SerializedName("Instruction")
	var instruction: String? = null

	@field:SerializedName("unit_id")
	var unitId: Int = 0

	@field:SerializedName("updatedAt")
	var updatedAt: String? = null

}