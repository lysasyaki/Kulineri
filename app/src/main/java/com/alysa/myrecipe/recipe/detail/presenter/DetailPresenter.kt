package com.alysa.myrecipe.recipe.detail.presenter

import com.alysa.myrecipe.core.domain.recipe.makanan.DataItem
import io.realm.Realm

class DetailPresenter {

    fun getDataByIdFromRealm(uniqueId: Int): DataItem? {
        val realm = Realm.getDefaultInstance()
        return realm.where(DataItem::class.java).equalTo("id", uniqueId).findFirst()
    }
}