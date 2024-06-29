package com.alysa.myrecipe.auth.model
data class RecipePreferances(
    val id: Int? = null,
    val name: String? = null,
    val desc: String? = null,
    val ingre: String? = null,
    val instru: String? = null,
    val category: Int? = null,
    val type: Int? = null,
    val image:  List<String>? = null
)
