package com.fitmate.fitmate.domain.model

data class ChatItem (
    val id: Int ?= null,
    val fitGroupId: String,
    val fitmateId: String,
    val message: String
)