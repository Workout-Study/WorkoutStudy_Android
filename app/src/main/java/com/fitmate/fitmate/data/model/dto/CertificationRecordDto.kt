package com.fitmate.fitmate.data.model.dto

import com.google.gson.annotations.SerializedName

data class CertificationRecordDto(
    @SerializedName("requestUserId") val requestUserId: String,
    @SerializedName("recordStartDate") val recordStartDate: String,
    @SerializedName("recordEndDate") val recordEndDate: String,
    @SerializedName("multiMediaEndPoints") val multiMediaEndPoints: List<String>,
)

data class CertificationRecordResponseDto(
    @SerializedName("isRegisterSuccess") val isRegisterSuccess: Boolean,
    @SerializedName("fitRecordId") val fitRecordId: Int?
)

data class CertificationTargetFitGroupResponseDto(
    @SerializedName("fitGroupDetails") val fitGroupItems: List<CertificationTargetFitGroupItemDto>,
)

data class CertificationTargetFitGroupItemDto(
    @SerializedName("fitGroupId") val fitGroupId: String,
    @SerializedName("fitGroupName") val fitGroupName: String,
)
