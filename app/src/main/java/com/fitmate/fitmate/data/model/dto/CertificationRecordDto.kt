package com.fitmate.fitmate.data.model.dto

import com.google.gson.annotations.SerializedName

data class CertificationRecordDto(
    @SerializedName("requestUserId") val requestUserId: String,
    @SerializedName("recordStartDate") val recordStartDate: String,
    @SerializedName("recordEndDate") val recordEndDate: String,
    @SerializedName("multiMediaEndPoints") val multiMediaEndPoints: List<String>,
)

data class CertificationRecordResponse(@SerializedName("isRegisterSuccess") val isRegisterSuccess: Boolean)
