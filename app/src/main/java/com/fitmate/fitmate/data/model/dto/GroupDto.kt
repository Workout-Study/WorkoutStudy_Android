package com.fitmate.fitmate.data.model.dto

import com.google.gson.annotations.SerializedName

data class GroupResponse(
    val content: List<Content>,
    val pageable: Pageable,
    val size: Int,
    val number: Int,
    val sort: Sort,
    val first: Boolean,
    val last: Boolean,
    val numberOfElements: Int,
    val empty: Boolean
)

data class Content(
    val presentFitMateCount: Int,
    val multiMediaEndPoints: List<String>,
    val fitGroupId: Int,
    val fitLeaderUserId: String,
    val fitGroupName: String,
    val penaltyAmount: Int,
    val penaltyAccountBankCode: String,
    val penaltyAccountNumber: String,
    val category: Int,
    val introduction: String,
    val cycle: Int,
    val frequency: Int,
    val createdAt: String,
    val maxFitMate: Int,
    val state: Boolean
)

data class Pageable(
    val pageNumber: Int,
    val pageSize: Int,
    val sort: Sort,
    val offset: Int,
    val unpaged: Boolean,
    val paged: Boolean
)

data class Sort(
    val empty: Boolean,
    val unsorted: Boolean,
    val sorted: Boolean
)

data class GroupDetailResponse(
    val presentFitMateCount: Int,
    val multiMediaEndPoints: List<String>,
    val fitGroupId: Int,
    val fitLeaderUserId: String,
    val fitGroupName: String,
    val penaltyAmount: Int,
    val penaltyAccountBankCode: String,
    val penaltyAccountNumber: String,
    val category: Int,
    val introduction: String,
    val cycle: Int,
    val frequency: Int,
    val createdAt: String,
    val maxFitMate: Int,
    val state: Boolean
)

data class MyFitResponse(
    @SerializedName("needVoteCertificationFitGroupList") val fitGroupList: List<MyFitGroupVote>
)

data class MyFitGroupVote(
    @SerializedName("fitGroupId") val groupId: Int,
    @SerializedName("fitGroupName") val groupName: String,
    @SerializedName("needVoteCertificationList") val certificationList: List<VoteCertification>
)

data class VoteCertification(
    @SerializedName("certificationId") val certificationId: Int,
    @SerializedName("recordId") val recordId: Int,
    @SerializedName("certificationRequestUserId") val requestUserId: String,
    @SerializedName("agreeCount") val agreeCount: Int,
    @SerializedName("disagreeCount") val disagreeCount: Int,
    @SerializedName("maxAgreeCount") val maxAgreeCount: Int,
    @SerializedName("voteEndDate") val voteEndDate: String,
    @SerializedName("recordMultiMediaEndPoints") val multiMediaEndPoints: List<String>
)

data class EachFitResponse(
    val fitCertificationDetails: List<GroupCertificationDetail>
)

data class GroupCertificationDetail(
    val certificationId: Int,
    val recordId: Int,
    val certificationRequestUserId: String,
    val isUserVoteDone: Boolean,
    val isUserAgree: Boolean,
    val agreeCount: Int,
    val disagreeCount: Int,
    val maxAgreeCount: Int,
    val fitRecordStartDate: String,
    val fitRecordEndDate: String,
    val voteEndDate: String
)