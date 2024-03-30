package com.fitmate.fitmate.data.model

import com.fitmate.fitmate.data.model.entity.ChatEntity
import com.fitmate.fitmate.domain.model.ChatItem

object ChatMapper {

    fun ChatItem.toEntity() = ChatEntity(
        id = id,
        fitGroupId = fitGroupId,
        fitmateId = fitmateId,
        message = message
    )

    fun ChatEntity.toDBChat() = ChatItem(
        id = id,
        fitGroupId = fitGroupId,
        fitmateId = fitmateId,
        message = message
    )

}