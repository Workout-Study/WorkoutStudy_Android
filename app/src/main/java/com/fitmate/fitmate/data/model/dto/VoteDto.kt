package com.fitmate.fitmate.data.model.dto

import com.google.gson.annotations.SerializedName

data class EachFitResponse(
    val fitCertificationDetails: List<GroupCertificationDetail>
)

data class GroupCertificationDetail(
    val certificationId: Int,
    val recordId: Int,
    val certificationRequestUserId: Int,
    val certificationRequestUserNickname: String,
    val isUserVoteDone: Boolean,
    val isUserAgree: Boolean,
    val agreeCount: Int,
    val disagreeCount: Int,
    val maxAgreeCount: Int,
    val thumbnailEndPoint: String,
    val fitRecordStartDate: String,
    val fitRecordEndDate: String,
    val voteEndDate: String
)

data class FitGroupProgressList(
    @SerializedName("fitMateUserId")
    val fitMateUserId: String,

    @SerializedName("certificationCount")
    val certificationCount: Int
)

data class VoteRequest(
    val requestUserId: Int,
    val agree: Boolean,
    val targetCategory: Int,
    val targetId: Int
)

data class VoteResponse(
    val isRegisterSuccess: Boolean
)



data class FitGroupProgress(
    @SerializedName("fitGroupId") val fitGroupId: Int,
    @SerializedName("fitGroupName") val fitGroupName: String,
    @SerializedName("cycle") val cycle: Int,
    @SerializedName("frequency") val frequency: Int,
    @SerializedName("fitCertificationProgresses") val fitCertificationProgresses: List<FitGroupProgressList>
)

data class MyFitGroupVote(
    @SerializedName("fitGroupId") val groupId: Int,
    @SerializedName("fitGroupName") val groupName: String,
    @SerializedName("needVoteCertificationList") val certificationList: List<VoteCertification>
)

data class VoteCertification(
    @SerializedName("certificationId") val certificationId: Int,
    @SerializedName("recordId") val recordId: Int,
    @SerializedName("certificationRequestUserId") val requestUserId: Int,
    @SerializedName("agreeCount") val agreeCount: Int,
    @SerializedName("disagreeCount") val disagreeCount: Int,
    @SerializedName("maxAgreeCount") val maxAgreeCount: Int,
    @SerializedName("voteEndDate") val voteEndDate: String,
    @SerializedName("recordMultiMediaEndPoints") val multiMediaEndPoints: List<String>
)

data class MyFitResponse(
    @SerializedName("needVoteCertificationFitGroupList") val fitGroupList: List<MyFitGroupVote>
)