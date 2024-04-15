package com.fitmate.fitmate.data.model

import com.fitmate.fitmate.data.model.dto.MyFitProgressResponseDto
import com.fitmate.fitmate.domain.model.FitProgressItem

object MyFitMapper {

    fun MyFitProgressResponseDto.toMyFitProgressResponse(): List<FitProgressItem> {
        return this.MyFitProgressResponse.map {
            FitProgressItem(
                itemId = it.fitGroupId,
                itemName = it.fitGroupName,
                thumbnailEndPoint = it.thumbnailEndPoint,
                cycle = it.cycle,
                frequency = it.frequency,
                certificationCount = it.certificationCount
            )
        }
    }
}