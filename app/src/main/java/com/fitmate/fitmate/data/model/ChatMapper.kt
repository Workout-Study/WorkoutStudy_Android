package com.fitmate.fitmate.data.model

import com.fitmate.fitmate.data.model.entity.ChatEntity
import com.fitmate.fitmate.domain.model.ChatItem

object ChatMapper {

    fun ChatItem.toEntity() = ChatEntity(
        messageId = messageId,
        fitGroupId = fitGroupId,
        userId = userId,
        message = message,
        messageTime = messageTime,
        messageType = messageType

    )

    fun ChatEntity.toDBChat() = ChatItem(
        messageId = messageId,
        fitGroupId = fitGroupId,
        userId = userId,
        message = message,
        messageTime = messageTime,
        messageType = messageType
    )

}