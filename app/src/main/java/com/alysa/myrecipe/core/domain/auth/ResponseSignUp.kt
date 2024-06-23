package com.alysa.myrecipe.core.domain.auth

import com.google.gson.annotations.SerializedName
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import io.realm.annotations.RealmClass
import java.io.Serializable

@RealmClass
open class ResponseSignUp: RealmObject(), Serializable {
	@PrimaryKey
	var id: Int = 0

	@field:SerializedName("name")
	var name: String? = null

	@field:SerializedName("age")
	var age: String? = null

	@field:SerializedName("username")
	var username: String? = null
}
