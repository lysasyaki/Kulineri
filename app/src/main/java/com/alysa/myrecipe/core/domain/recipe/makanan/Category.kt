package com.alysa.myrecipe.core.domain.recipe.makanan

import com.google.gson.annotations.SerializedName
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import java.io.Serializable

open class Category(

	@field:SerializedName("name")
	var name: String? = null
)