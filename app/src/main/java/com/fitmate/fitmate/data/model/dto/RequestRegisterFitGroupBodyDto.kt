package com.fitmate.fitmate.data.model.dto

import com.google.gson.annotations.SerializedName

data class RequestRegisterFitGroupBodyDto(
    @SerializedName("requestUserId") val requestUserId: String,
    @SerializedName("fitGroupName") val fitGroupName: String,
    @SerializedName("penaltyAmount") val penaltyAmount: Long,
    @SerializedName("penaltyAccountBankCode") val penaltyAccountBankCode: String,
    @SerializedName("penaltyAccountNumber") val penaltyAccountNumber: String,
    @SerializedName("category") val category: Int,
    @SerializedName("introduction") val introduction: String,
    @SerializedName("cycle") val cycle: Int? = null,
    @SerializedName("frequency") val frequency: Int,
    @SerializedName("maxFitMate") val maxFitMate: Int,
    @SerializedName("multiMediaEndPoints") val multiMediaEndPoints: List<String>,
)

data class RegisterFitGroupResponseDto(
    @SerializedName("isRegisterSuccess") val isRegisterSuccess: Boolean
)
