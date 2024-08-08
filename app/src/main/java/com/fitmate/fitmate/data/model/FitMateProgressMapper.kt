package com.fitmate.fitmate.data.model

import com.fitmate.fitmate.data.model.dto.FitMateProgressDto
import com.fitmate.fitmate.domain.model.FitMateProgress
import com.fitmate.fitmate.domain.model.FitMateProgressContent

object FitMateProgressMapper {
    fun mapFitMateProgressDtoToEntity(dto: FitMateProgressDto): FitMateProgress {
        return FitMateProgress(
            fitGroupId = dto.fitGroupId,
            fitGroupName = dto.fitGroupName,
            cycle = dto.cycle,
            frequency = dto.frequency,
            fitCertificationProgresses = dto.fitCertificationProgresses.map { contentDto ->
                FitMateProgressContent(
                    fitMateUserId = contentDto.fitMateUserId,
                    fitMateUserNickname = contentDto.fitMateUserNickname,
                    certificationCount = contentDto.certificationCount
                )
            }
        )
    }
}