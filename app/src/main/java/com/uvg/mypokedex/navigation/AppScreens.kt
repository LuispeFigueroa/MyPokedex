package com.uvg.mypokedex.navigation

object AppScreens {
    const val HOME = "home"
    const val DETAIL = "detail/{id}"
    fun detail(id: Int) = "detail/$id"
    const val ORDER = "order"
    const val AUTH = "auth"
    const val TRADE = "trade"
    const val TRADE_SELECT = "trade/select"
    const val TRADE_SHOW_CODE = "trade/show_code/{pokemonId}/{pokemonName}"
    fun showCode(pokemonId: Int, pokemonName: String) = "trade/show_code/$pokemonId/$pokemonName"
    const val TRADE_ENTER_CODE = "trade/enter_code"
}