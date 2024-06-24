package com.alysa.myrecipe.core.domain.Favorite

import com.alysa.myrecipe.core.domain.recipe.Data
import com.google.gson.annotations.SerializedName
import io.realm.RealmList
import io.realm.RealmObject
import java.io.Serializable

open class ResponseFavorite: RealmObject(), Serializable {

	@field:SerializedName("data")
	var data: RealmList<DataFavorite>? = null

	@field:SerializedName("message")
	var message: String? = null
}