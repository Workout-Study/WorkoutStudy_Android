package com.fitmate.fitmate.domain.model

import com.google.gson.annotations.SerializedName

data class FitOffRequest(
    val requestUserId: Int,
    val fitOffStartDate: String,
    val fitOffEndDate: String,
    val fitOffReason: String
)

data class FitOffResponse(
    val isRegisterSuccess: Boolean,
    val fitOffId: Int?
)
