package com.fitmate.fitmate.domain.model

import com.google.gson.annotations.SerializedName

data class FitProgressItem(
    val itemId: Int,
    val itemName: String,
    val thumbnailEndPoint: String,
    val maxFitMate: String,
    val presentFitMateCount: String,
    val cycle: Int,
    val frequency: Int,
    val certificationCount: Int,
)
