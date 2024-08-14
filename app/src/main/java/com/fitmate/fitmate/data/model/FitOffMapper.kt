package com.fitmate.fitmate.data.model

import com.fitmate.fitmate.data.model.dto.FitOffRequestDto
import com.fitmate.fitmate.data.model.dto.FitOffResponseDto
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
}