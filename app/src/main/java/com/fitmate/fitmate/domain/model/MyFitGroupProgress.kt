package com.fitmate.fitmate.domain.model

data class MyFitGroupProgress(
    val title: String,
    val image: String? = null,
    val maxProgress: Int,
    val nowProgress: Int,
)
