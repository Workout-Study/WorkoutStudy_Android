package com.fitmate.fitmate.domain.model


data class CertificationRecord(
    val requestUserId: String,
    val recordStartDate: String,
    val recordEndDate: String,
    val multiMediaEndPoints: List<String>,
)
