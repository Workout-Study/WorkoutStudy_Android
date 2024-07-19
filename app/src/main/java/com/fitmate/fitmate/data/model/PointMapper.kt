package com.fitmate.fitmate.data.model

import com.fitmate.fitmate.data.model.dto.PointDto
import com.fitmate.fitmate.data.model.dto.PointHistoryContentDto
import com.fitmate.fitmate.data.model.dto.PointHistoryDto
import com.fitmate.fitmate.domain.model.Point
import com.fitmate.fitmate.domain.model.PointHistory
import com.fitmate.fitmate.domain.model.PointHistoryContent

object PointMapper {
    fun PointDto.toPoint(): Point {
        return Point(
            pointId = this.pointId,
            pointOwnerId = this.pointOwnerId,
            pointOwnerType = this.pointOwnerType,
            balance = this.balance
        )
    }

    fun PointHistoryContentDto.toPointHistoryContent(): PointHistoryContent {
        return PointHistoryContent(
            tradeType = this.tradeType,
            amount = this.amount,
            message = this.message,
            depositUserId = this.depositUserId,
            depositUserNickname = this.depositUserNickname,
            createdAt = this.createdAt
        )
    }

    fun PointHistoryDto.toPointHistory(): PointHistory {
        return PointHistory(
            pageNumber = this.pageNumber,
            pageSize = this.pageSize,
            hasNext = this.hasNext,
            pointBalance = this.pointBalance,
            content = this.content.map { it.toPointHistoryContent() }
        )
    }
}