package com.alysa.myrecipe.core.domain.auth

import com.google.gson.annotations.SerializedName
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import io.realm.annotations.RealmClass
import java.io.Serializable

@RealmClass
open class ResponseSignIn: RealmObject(), Serializable {
	@PrimaryKey
	var id: Int = 0

	@SerializedName("username")
	var username: String? = null

	@SerializedName("token")
	var token: String = ""

	@SerializedName("refreshToken")
	var refreshToken: String? = null
}
