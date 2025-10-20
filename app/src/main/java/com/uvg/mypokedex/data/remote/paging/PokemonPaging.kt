package com.uvg.mypokedex.data.remote.paging

object PokemonPaging {

    const val PAGE_SIZE: Int = 40

    const val LOAD_AHEAD_THRESHOLD: Int = 5

    fun nextOffset(currentItemCount: Int): Int = currentItemCount

    fun shouldLoadNextPage(visibleLastIndex: Int, totalCount: Int): Boolean {
        return totalCount - visibleLastIndex <= LOAD_AHEAD_THRESHOLD
    }
}
