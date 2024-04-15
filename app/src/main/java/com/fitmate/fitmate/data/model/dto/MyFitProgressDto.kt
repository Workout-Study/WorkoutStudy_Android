package com.fitmate.fitmate.data.model.dto

import com.google.gson.annotations.SerializedName

data class MyFitProgressResponseDto(
    @SerializedName("fitCertificationProgresses") val MyFitProgressResponse: List<MyFitProgressDto>,
)

data class MyFitProgressDto(
    @SerializedName("fitGroupId") val fitGroupId: Int,
    @SerializedName("fitGroupName") val fitGroupName: String,
    @SerializedName("thumbnailEndPoint") val thumbnailEndPoint: String,
    @SerializedName("cycle") val cycle: Int,
    @SerializedName("frequency") val frequency: Int,
    @SerializedName("certificationCount") val certificationCount: Int,
)
