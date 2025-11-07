package com.uvg.mypokedex.domain.model

data class Exchange(
    val id: String,
    val code: String,
    val userA: String,
    val userB: String?,           // null hasta que B se una
    val offerAId: Int,
    val offerAName: String,
    val offerBId: Int?,           // null hasta que B elija
    val offerBName: String?,      // null hasta que B elija
    val status: Status
) {
    enum class Status { PROPOSED, JOINED, COMMITTING, COMMITTED, CANCELLED, EXPIRED }
}