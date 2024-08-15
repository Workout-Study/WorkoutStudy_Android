package com.fitmate.fitmate.data.model

import com.fitmate.fitmate.data.model.dto.FitMateProgressDto
import com.fitmate.fitmate.data.model.dto.FitOffListDto
import com.fitmate.fitmate.data.model.dto.FitOffRequestDto
import com.fitmate.fitmate.data.model.dto.FitOffResponseDto
import com.fitmate.fitmate.domain.model.FitMateProgress
import com.fitmate.fitmate.domain.model.FitMateProgressContent
import com.fitmate.fitmate.domain.model.FitOff
import com.fitmate.fitmate.domain.model.FitOffList
import com.fitmate.fitmate.domain.model.FitOffRequest
import com.fitmate.fitmate.domain.model.FitOffResponse

object FitOffMapper {

    fun FitOffRequest.toFitOffRequestDto():FitOffRequestDto {
        return FitOffRequestDto(
            requestUserId = this.requestUserId,
            fitOffStartDate = this.fitOffStartDate,
            fitOffEndDate = this.fitOffEndDate,
            fitOffReason = this.fitOffReason
        )
    }

    fun FitOffResponseDto.toFitOffResponse():FitOffResponse {
        return FitOffResponse(
            isRegisterSuccess,
            fitOffId
        )
    }

    fun mapFitOffListDtoToEntity(dto: FitOffListDto): FitOffList {
        return FitOffList(
            content = dto.content.map { fitOffDto ->
                FitOff(
                    fitOffId = fitOffDto.fitOffId,
                    userId = fitOffDto.userId,
                    fitOffStartDate = fitOffDto.fitOffStartDate,
                    fitOffEndDate = fitOffDto.fitOffEndDate,
                    fitOffReason = fitOffDto.fitOffReason
                )
            }
        )
    }
}