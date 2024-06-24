package com.alysa.myrecipe.core.domain.Favorite

import com.google.gson.annotations.SerializedName
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import java.io.Serializable

open class DataFavorite: RealmObject(), Serializable {
	@PrimaryKey
	var id: Int = 0

	@field:SerializedName("createdAt")
	var createdAt: String? = null

	@field:SerializedName("recipe_id")
	var recipeId: Int = 0

	@field:SerializedName("user_id")
	var userId: Int = 0

	@field:SerializedName("updatedAt")
	var updatedAt: String? = null
}