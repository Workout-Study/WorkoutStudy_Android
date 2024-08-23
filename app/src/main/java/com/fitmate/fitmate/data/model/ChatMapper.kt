package com.fitmate.fitmate.data.model

import com.fitmate.fitmate.data.model.dto.FitMateKickRequestUserIdDto
import com.fitmate.fitmate.data.model.dto.ResponseFitMateKickDto
import com.fitmate.fitmate.data.model.entity.ChatEntity
import com.fitmate.fitmate.domain.model.ChatItem
import com.fitmate.fitmate.domain.model.FitMateKickRequestUserId
import com.fitmate.fitmate.domain.model.ResponseFitMateKick

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

    fun FitMateKickRequestUserId.toDto() = FitMateKickRequestUserIdDto(
        requestUserId = this.requestUserId
    )

    fun ResponseFitMateKickDto.toEntity() = ResponseFitMateKick(
        isKickSuccess = this.isKickSuccess
    )

}