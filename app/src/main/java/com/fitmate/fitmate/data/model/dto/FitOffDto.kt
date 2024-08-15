package com.fitmate.fitmate.data.model.dto

import com.google.gson.annotations.SerializedName

data class FitOffRequestDto(
    @SerializedName("requestUserId")
    val requestUserId: Int,
    @SerializedName("fitOffStartDate")
    val fitOffStartDate: String,
    @SerializedName("fitOffEndDate")
    val fitOffEndDate: String,
    @SerializedName("fitOffReason")
    val fitOffReason: String
)

data class FitOffResponseDto(
    @SerializedName("isRegisterSuccess")
    val isRegisterSuccess: Boolean,
    @SerializedName("fitOffId")
    val fitOffId: Int?
)

data class FitOffListDto(
    @SerializedName("content")
    val content: List<FitOffDto>
)

data class FitOffDto(
    @SerializedName("fitOffId") val fitOffId: Int,
    @SerializedName("userId") val userId: Int,
    @SerializedName("fitOffStartDate") val fitOffStartDate: String,
    @SerializedName("fitOffEndDate") val fitOffEndDate: String,
    @SerializedName("fitOffReason") val fitOffReason: String
)
