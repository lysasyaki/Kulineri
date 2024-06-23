package com.alysa.myrecipe.core.domain.recipe

import com.google.gson.annotations.SerializedName
import io.realm.RealmList
import io.realm.RealmObject
import java.io.Serializable

open class ResepResponse: RealmObject(), Serializable {

	@field:SerializedName("data")
	var data: RealmList<Data>? = null

	@field:SerializedName("message")
	var message: String? = null

}