package com.fitmate.fitmate.domain.model

data class MyGroupNewsResponse(
    val pageNumber: Int,
    val pageSize: Int,
    val hasNext: Boolean,
    val content: List<MyGroupNews>
)

data class MyGroupNews(
    val fitGroupId: Int,
    val certificationRequestUserId: Int,
    val certificationRequestUserNickname: String,
    val thumbnailEndPoint: String,
    val certificationStatus: String,
    val agreeCount: Int,
    val disagreeCount: Int,
    val maxAgreeCount: Int,
    val isUserVoteDone: Boolean,
    val isUserAgree: Boolean,
    val issueDate: String
)
