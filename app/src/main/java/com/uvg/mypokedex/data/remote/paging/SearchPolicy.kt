package com.uvg.mypokedex.data.remote.paging

object SearchPolicy {
    const val MIN_LOCAL_QUERY_LEN: Int = 2

    fun isExactQuery(query: String): Boolean = query.isNotBlank() && !query.contains(' ')
    fun canDoLocalContains(query: String): Boolean = query.length >= MIN_LOCAL_QUERY_LEN
}