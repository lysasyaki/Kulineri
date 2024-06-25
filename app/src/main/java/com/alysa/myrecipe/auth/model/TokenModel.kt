package com.alysa.myrecipe.auth.model

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class UserToken(
    @PrimaryKey var id: String = "user_token",
    var token: String = ""
) : RealmObject()
