package com.uvg.mypokedex.navigation

object AppScreens {
    const val HOME = "home"
    const val DETAIL = "detail/{id}"
    fun detail(id: Int) = "detail/$id"
    const val ORDER = "order"
}