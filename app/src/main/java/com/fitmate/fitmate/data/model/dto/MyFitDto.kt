package com.fitmate.fitmate.data.model.dto

import com.google.gson.annotations.SerializedName

data class MyFitProgressResponseDto(
    @SerializedName("fitCertificationProgresses") val myFitProgressResponse: List<MyFitProgressDto>,
)

data class MyFitProgressDto(
    @SerializedName("fitGroupId") val fitGroupId: Int,
    @SerializedName("fitGroupName") val fitGroupName: String,
    @SerializedName("thumbnailEndPoint") val thumbnailEndPoint: String,
    @SerializedName("cycle") val cycle: Int,
    @SerializedName("frequency") val frequency: Int,
    @SerializedName("certificationCount") val certificationCount: Int,
)

data class MyFitHistoryResponseDto(
    @SerializedName("content") val content: List<MyFitCertificationContent>,
    @SerializedName("pageable") val pageable: List<MyFitCertificationContent>,
)

data class MyFitCertificationContent(
    @SerializedName("fitRecordId") val fitRecordId: Int,
    @SerializedName("recordStartDate") val recordStartDate: String,
    @SerializedName("recordEndDate") val recordEndDate: String,
    @SerializedName("createdAt") val createdAt: String,
    @SerializedName("multiMediaEndPoints") val multiMediaEndPoints: List<String>,
)