package com.fitmate.fitmate.data.model.dto

import com.google.gson.annotations.SerializedName

data class PointDto(
    @SerializedName("walletId") val pointId: Int,
    @SerializedName("walletOwnerId") val pointOwnerId: Int,
    @SerializedName("walletOwnerType") val pointOwnerType: String,
    @SerializedName("balance") val balance: Long,
)

data class PointHistoryDto(
    @SerializedName("pageNumber") val pageNumber: Int,
    @SerializedName("pageSize") val pageSize: Int,
    @SerializedName("hasNext") val hasNext: Boolean,
    @SerializedName("walletBalance") val pointBalance: Long,
    @SerializedName("content") val content: List<PointHistoryContentDto>,
)

data class PointHistoryContentDto(
    @SerializedName("tradeType") val tradeType: String,
    @SerializedName("amount") val amount: Long,
    @SerializedName("message") val message: String,
    @SerializedName("depositUserId") val depositUserId: Int?,
    @SerializedName("depositUserNickname") val depositUserNickname: String?,
    @SerializedName("createdAt") val createdAt: String,
)

