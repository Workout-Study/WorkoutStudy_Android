package com.fitmate.fitmate.domain.model

data class FitGroup(
    val groupName: String,
    val currentMessageTime: String,
    val currentMessage: String,
    val fitMateId: Int,
    val fitGroupId: Int,
)
