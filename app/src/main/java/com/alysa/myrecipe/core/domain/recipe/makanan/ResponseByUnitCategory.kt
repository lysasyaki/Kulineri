package com.alysa.myrecipe.core.domain.recipe.makanan

import com.google.gson.annotations.SerializedName
import io.realm.RealmList
import io.realm.RealmObject
import java.io.Serializable

open class ResponseByUnitCategory(

	@field:SerializedName("data")
	var data: List<DataItem>? = null,

	@field:SerializedName("message")
	var message: String? = null

)