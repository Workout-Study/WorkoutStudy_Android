package com.fitmate.fitmate.domain.model

data class VoteItem(
    val title: String,
    val fitMate: String,
    val percent: Int,
    val time: String,
    val image: String,
    val groupId: Int,
)
