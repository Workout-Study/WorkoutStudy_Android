package com.fitmate.fitmate.domain.model

data class FitProgressItem(
    val itemId: Int,
    val itemName: String,
    val thumbnailEndPoint: String,
    val cycle: Int,
    val frequency: Int,
    val certificationCount: Int,
)
