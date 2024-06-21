package com.alysa.myrecipe.core.view

import com.alysa.myrecipe.core.domain.auth.ResponseSignIn
import com.alysa.myrecipe.core.domain.auth.ResponseSignUp
import com.alysa.myrecipe.core.utils.ResultState

sealed interface viewSignUp{
    fun displaySignUp(result: ResultState<List<ResponseSignUp>>)
}

sealed interface viewSignIn{
    fun displaySignIn(result: ResultState<List<ResponseSignIn>>)
}