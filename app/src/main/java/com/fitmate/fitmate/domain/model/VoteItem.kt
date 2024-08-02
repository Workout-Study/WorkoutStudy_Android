package com.fitmate.fitmate.domain.model

import com.fitmate.fitmate.data.model.dto.GroupVoteCertificationDetailDto


data class VoteItem(
    val title: String,
    val fitMate: String,
    val percent: Int,
    val time: String,
    val image: String,
    val groupId: Int,
    val startTime: String?,
    val endTime: String?,
)
data class EachVoteCertificationResponse(
    val fitCertificationDetails: List<GroupVoteCertificationDetail>
)

data class GroupVoteCertificationDetail(
    val certificationId: Int,
    val recordId: Int,
    val certificationRequestUserId: Int,
    val certificationRequestUserNickname: String,
    val isUserVoteDone: Boolean,
    val isUserAgree: Boolean,
    val agreeCount: Int,
    val disagreeCount: Int,
    val maxAgreeCount: Int,
    val thumbnailEndPoint: List<String>?,
    val fitRecordStartDate: String,
    val fitRecordEndDate: String,
    val voteEndDate: String
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

data class VoteUpdateResponse(
    val isUpdateSuccess: Boolean
)