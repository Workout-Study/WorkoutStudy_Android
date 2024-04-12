package com.fitmate.fitmate.domain.model

data class CertificationRecordResponse(
    val isRegisterSuccess: Boolean,
    val fitRecordId: Int?
)

data class FitGroupItem(
   val fitGroupId: String,
   val fitGroupName: String,
)