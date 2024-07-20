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
    val userId: Int,
    val message: String,
    val messageTime: String,
    val messageType: String
)

data class RetrieveFitGroup(
    @SerializedName("ID") val id: Int,
    @SerializedName("FitLeaderUserID") val fitLeaderUserID: Int,
    @SerializedName("FitGroupName") val fitGroupName: String,
    @SerializedName("Category") val category: Int,
    @SerializedName("Cycle") val cycle: Int,
    @SerializedName("Frequency") val frequency: Int,
    @SerializedName("PresentFitMateCount") val presentFitMateCount: Int,
    @SerializedName("MaxFitMate") val maxFitMate: Int,
    @SerializedName("State") val state: Boolean,
    @SerializedName("CreatedAt") val createdAt: String,
    @SerializedName("CreatedBy") val createdBy: String,
    @SerializedName("UpdatedAt") val updatedAt: String,
    @SerializedName("UpdatedBy") val updatedBy: String
)