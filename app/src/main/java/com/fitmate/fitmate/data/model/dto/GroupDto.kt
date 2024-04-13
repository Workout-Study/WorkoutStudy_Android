package com.fitmate.fitmate.data.model.dto

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