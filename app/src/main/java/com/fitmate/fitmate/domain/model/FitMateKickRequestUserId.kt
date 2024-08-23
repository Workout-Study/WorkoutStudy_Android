package com.fitmate.fitmate.domain.model

import com.google.gson.annotations.SerializedName

data class FitMateKickRequestUserId(
    val requestUserId: Int
)

data class ResponseFitMateKick(
    val isKickSuccess: Boolean
)

