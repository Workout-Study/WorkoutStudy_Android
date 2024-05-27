package com.fitmate.fitmate.data.model.dto

import com.google.gson.annotations.SerializedName


data class FitRecordHistoryResponse(
    @SerializedName("fitRecordDetailResponseDtoList") val fitRecordDetailResponseDtoList: List<FitRecordHistoryDetail>
)

data class FitRecordHistoryDetail(
    @SerializedName("fitRecordId") val fitRecordId: Int,
    @SerializedName("recordStartDate") val recordStartDate: String,
    @SerializedName("recordEndDate") val recordEndDate: String,
    @SerializedName("createdAt") val createdAt: String,
    @SerializedName("multiMediaEndPoints") val multiMediaEndPoints: List<String>,
    @SerializedName("registeredFitCertifications") val registeredFitCertifications: List<FitRecordHistoryGroupInfo>
)

data class FitRecordHistoryGroupInfo(
    @SerializedName("fitGroupId") val fitGroupId: Int,
    @SerializedName("fitGroupName") val fitGroupName: String,
    @SerializedName("fitCertificationId") val fitCertificationId: Int,
    @SerializedName("certificationStatus") val certificationStatus: String,
    @SerializedName("createdAt") val createdAt: String,
    @SerializedName("voteEndDate") val voteEndDate: String
)