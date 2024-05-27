package com.fitmate.fitmate.domain.model

data class MyFitRecordHistory(
    val fitRecordDetailResponseDtoList: List<MyFitRecordHistoryDetail>
)

data class MyFitRecordHistoryDetail(
    val fitRecordId: Int,
    val recordStartDate: String,
    val recordEndDate: String,
    val createdAt: String,
    val multiMediaEndPoints: List<String>,
    val registeredFitCertifications: List<MyFitRecordHistoryGroupInfo>
)

data class MyFitRecordHistoryGroupInfo(
    val fitGroupId: Int,
    val fitGroupName: String,
    val fitCertificationId: Int,
    val certificationStatus: String,
    val createdAt: String,
    val voteEndDate: String
)