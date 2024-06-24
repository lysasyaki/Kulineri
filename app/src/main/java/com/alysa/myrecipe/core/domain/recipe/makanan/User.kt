package com.alysa.myrecipe.core.domain.recipe.makanan

import com.google.gson.annotations.SerializedName
import io.realm.RealmObject
import java.io.Serializable

open class User: RealmObject(), Serializable {

	@field:SerializedName("name")
	var name: String? = null
}