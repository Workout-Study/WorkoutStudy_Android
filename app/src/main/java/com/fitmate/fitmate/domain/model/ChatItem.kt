package com.fitmate.fitmate.domain.model

import java.io.Serializable
import java.time.Instant
import java.time.LocalDateTime

data class ChatItem (
    val messageId: String,
    val fitGroupId: String,
    val fitmateId: String,
    val message: String,
    val messageTime: LocalDateTime,
    val messageType: String
) : Serializable