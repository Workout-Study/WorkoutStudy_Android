package com.fitmate.fitmate.data.model.dto

import com.google.gson.annotations.SerializedName
import java.io.Serializable


// Get Fit Group Detail
data class GetFitGroupDetail(
    @SerializedName("presentFitMateCount") val presentFitMateCount: Int,
    @SerializedName("multiMediaEndPoints") val multiMediaEndPoints: List<String>,
    @SerializedName("fitGroupId") val fitGroupId: Int,
    @SerializedName("fitLeaderUserId") val fitLeaderUserId: String,
    @SerializedName("fitGroupName") val fitGroupName: String,
    @SerializedName("penaltyAmount") val penaltyAmount: Int,
    @SerializedName("penaltyAccountBankCode") val penaltyAccountBankCode: String,
    @SerializedName("penaltyAccountNumber") val penaltyAccountNumber: String,
    @SerializedName("category") val category: Int,
    @SerializedName("introduction") val introduction: String,
    @SerializedName("cycle") val cycle: Int,
    @SerializedName("frequency") val frequency: Int,
    @SerializedName("createdAt") val createdAt: String,
    @SerializedName("maxFitMate") val maxFitMate: Int,
    @SerializedName("state") val state: Boolean
)

// Get Fit Mate List - FitGroupDetail, FitLeaderDetail, FitMateDetail
data class GetFitMateList(
    @SerializedName("fitGroupId") val fitGroupId: Int,
    @SerializedName("fitLeaderDetail") val fitLeaderDetail: FitLeaderDetail,
    @SerializedName("fitMateDetails") val fitMateDetails: List<FitMateDetail>
):Serializable

data class FitLeaderDetail(
    @SerializedName("fitLeaderUserId") val fitLeaderUserId: String,
    @SerializedName("fitLeaderUserNickname") val fitLeaderUserNickname: String,
    @SerializedName("createdAt") val createdAt: String
)

data class FitMateDetail(
    @SerializedName("fitMateId") val fitMateId: Int,
    @SerializedName("fitMateUserId") val fitMateUserId: String,
    @SerializedName("fitMateUserNickname") val fitMateUserNickname: String,
    @SerializedName("fitMateUserProfileImageUrl") val fitMateUserProfileImageUrl: String?,
    @SerializedName("createdAt") val createdAt: String
)

// Fit Group Filter
data class FitGroupFilter(
    @SerializedName("content") val content: List<FitGroupDetail>,
    @SerializedName("pageable") val pageable: Pageable,
    @SerializedName("size") val size: Int,
    @SerializedName("number") val number: Int,
    @SerializedName("sort") val sort: Sort,
    @SerializedName("numberOfElements") val numberOfElements: Int,
    @SerializedName("first") val first: Boolean,
    @SerializedName("last") val last: Boolean,
    @SerializedName("empty") val empty: Boolean
)

data class FitGroupDetail(
    @SerializedName("presentFitMateCount") val presentFitMateCount: Int,
    @SerializedName("multiMediaEndPoints") val multiMediaEndPoints: List<String>,
    @SerializedName("fitGroupId") val fitGroupId: Int,
    @SerializedName("fitLeaderUserId") val fitLeaderUserId: Int,
    @SerializedName("fitGroupName") val fitGroupName: String,
    @SerializedName("penaltyAmount") val penaltyAmount: Int,
    @SerializedName("penaltyAccountBankCode") val penaltyAccountBankCode: String,
    @SerializedName("penaltyAccountNumber") val penaltyAccountNumber: String,
    @SerializedName("category") val category: Int,
    @SerializedName("introduction") val introduction: String,
    @SerializedName("cycle") val cycle: Int,
    @SerializedName("frequency") val frequency: Int,
    @SerializedName("createdAt") val createdAt: String,
    @SerializedName("maxFitMate") val maxFitMate: Int,
    @SerializedName("state") val state: Boolean
)

data class Pageable(
    @SerializedName("pageNumber") val pageNumber: Int,
    @SerializedName("pageSize") val pageSize: Int,
    @SerializedName("sort") val sort: Sort,
    @SerializedName("offset") val offset: Long,
    @SerializedName("paged") val paged: Boolean,
    @SerializedName("unpaged") val unpaged: Boolean
)

data class Sort(
    @SerializedName("empty") val empty: Boolean,
    @SerializedName("sorted") val sorted: Boolean,
    @SerializedName("unsorted") val unsorted: Boolean
)

data class FitMateRequest(
    val requestUserId: Int,
    val fitGroupId: Int
)

data class RegisterResponse(
    @SerializedName("isRegisterSuccess") val isRegisterSuccess: Boolean
)

data class FitMateKickRequestUserIdDto(
    @SerializedName("requestUserId") val requestUserId: Int
)

data class ResponseFitMateKickDto(
    @SerializedName("isKickSuccess") val isKickSuccess: Boolean
)