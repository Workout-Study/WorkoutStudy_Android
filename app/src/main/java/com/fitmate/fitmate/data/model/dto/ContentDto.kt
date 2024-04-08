package com.fitmate.fitmate.data.model.dto

import com.google.gson.annotations.SerializedName
import java.time.LocalDateTime

data class FitGroup(
    @SerializedName("ID") val id: Int,
    @SerializedName("FitGroupName") val fitGroupName: String,
    @SerializedName("MaxFitMate") val maxFitMate: Int,
    @SerializedName("CreatedAt") val createdAt: String,
    @SerializedName("CreatedBy") val createdBy: String,
    @SerializedName("UpdatedAt") val updatedAt: String,
    @SerializedName("UpdatedBy") val updatedBy: String
)