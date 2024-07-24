package com.fitmate.fitmate.domain.model

data class RequestRegisterFitGroupBody(
    val requestUserId: Int,
    val fitGroupName: String,
    val penaltyAmount: Long,
    val category: Int,
    val introduction: String,
    val cycle: Int? = null,
    val frequency: Int,
    val maxFitMate: Int,
    val multiMediaEndPoints: List<String>,
)

data class ResponseRegisterFitGroup(
    val isRegisterSuccess: Boolean,
)

