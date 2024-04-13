package com.fitmate.fitmate.domain.model

import com.google.gson.annotations.SerializedName

data class ResisterCertificationRecord(
    val requestUserId: String,
    val fitRecordId: Int,
    val fitGroupIds: List<String>,
)
