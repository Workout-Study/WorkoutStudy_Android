package com.fitmate.fitmate.data.model.dto

import com.google.gson.annotations.SerializedName

data class MyGroupNewsResponseDto(
    @SerializedName("pageNumber")
    val pageNumber: Int,
    @SerializedName("pageSize")
    val pageSize: Int,
    @SerializedName("hasNext")
    val hasNext: Boolean,
    @SerializedName("content")
    val content: List<MyGroupNewsDto>
)

data class MyGroupNewsDto(
    @SerializedName("fitGroupId")
    val fitGroupId: Int,
    @SerializedName("certificationRequestUserId")
    val certificationRequestUserId: Int,
    @SerializedName("certificationRequestUserNickname")
    val certificationRequestUserNickname: String,
    @SerializedName("thumbnailEndPoint")
    val thumbnailEndPoint: String,
    @SerializedName("certificationStatus")
    val certificationStatus: String,
    @SerializedName("agreeCount")
    val agreeCount: Int,
    @SerializedName("disagreeCount")
    val disagreeCount: Int,
    @SerializedName("maxAgreeCount")
    val maxAgreeCount: Int,
    @SerializedName("isUserVoteDone")
    val isUserVoteDone: Boolean,
    @SerializedName("isUserAgree")
    val isUserAgree: Boolean,
    @SerializedName("issueDate")
    val issueDate: String
)
