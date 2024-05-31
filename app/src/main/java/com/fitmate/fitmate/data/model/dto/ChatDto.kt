package com.fitmate.fitmate.data.model.dto

import com.google.gson.annotations.SerializedName

data class FitGroup(
    @SerializedName("ID") val id: Int,
    @SerializedName("FitGroupName") val fitGroupName: String,
    @SerializedName("MaxFitMate") val maxFitMate: Int,
    @SerializedName("CreatedAt") val createdAt: String,
    @SerializedName("CreatedBy") val createdBy: String,
    @SerializedName("UpdatedAt") val updatedAt: String,
    @SerializedName("UpdatedBy") val updatedBy: String
)

data class ChatResponse (
    val messages: List<Message>
)

data class Message (
    val messageId: String,
    val fitGroupId: Int,
    val fitMateId: Int,
    val message: String,
    val messageTime: String,
    val messageType: String
)

data class RetrieveFitGroup(
    @SerializedName("category") val category: Int,
    @SerializedName("createdAt") val createdAt: String,
    @SerializedName("createdBy") val createdBy: String,
    @SerializedName("cycle") val cycle: Int,
    @SerializedName("fitGroupName") val fitGroupName: String,
    @SerializedName("fitLeaderUserID") val fitLeaderUserID: Int,
    @SerializedName("frequency") val frequency: Int,
    @SerializedName("id") val id: Int,
    @SerializedName("maxFitMate") val maxFitMate: Int,
    @SerializedName("presentFitMateCount") val presentFitMateCount: Int,
    @SerializedName("state") val state: Boolean,
    @SerializedName("updatedAt") val updatedAt: String,
    @SerializedName("updatedBy") val updatedBy: String
)