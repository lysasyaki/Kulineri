package com.alysa.myrecipe.core.domain.auth

import io.realm.RealmObject
import java.io.Serializable

open class ResponseSignOut: RealmObject(), Serializable {
	var auth: Boolean? = null
	var message: String? = null
	var token: String? = null
}

