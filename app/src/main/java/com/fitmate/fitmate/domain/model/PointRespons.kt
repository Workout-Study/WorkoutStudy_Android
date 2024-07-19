package com.fitmate.fitmate.domain.model

data class Point(
    val pointId: Int,
    val pointOwnerId: Int,
    val pointOwnerType: String,
    val balance: Long,
)

data class PointHistory(
    val pageNumber: Int,
    val pageSize: Int,
    val hasNext: Boolean,
    val pointBalance: Long,
    val content: List<PointHistoryContent>,
)

data class PointHistoryContent(
    val tradeType: String,
    val amount: Long,
    val message: String,
    val depositUserId: Int,
    val depositUserNickname: String,
    val createdAt: String,
)

enum class PointType(val value: String) {
    GROUP("GROUP"), USER("USER")
}

enum class TradeType(val value: String) {
    DEPOSIT("DEPOSIT"), WITHDRAW("WITHDRAW")
}

