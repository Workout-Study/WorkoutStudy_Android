package com.fitmate.fitmate.data.model

import com.fitmate.fitmate.data.model.dto.RegisterFitGroupResponseDto
import com.fitmate.fitmate.data.model.dto.RequestRegisterFitGroupBodyDto
import com.fitmate.fitmate.data.model.dto.UpdateFitGroupResponseDto
import com.fitmate.fitmate.domain.model.RequestRegisterFitGroupBody
import com.fitmate.fitmate.domain.model.ResponseRegisterFitGroup
import com.fitmate.fitmate.domain.model.UpdateFitGroupResponse

object MakeFitGroupMapper {

    // view layer의 RegisterFitGroup 데이터를 RegisterFitGroupDto로 변환하는 메서드
    fun RequestRegisterFitGroupBody.toRegisterFitGroupDto(): RequestRegisterFitGroupBodyDto {
        return RequestRegisterFitGroupBodyDto(
            requestUserId = this.requestUserId,
            fitGroupName = this.fitGroupName,
            penaltyAmount = this.penaltyAmount,
            category = this.category,
            introduction = this.introduction,
            cycle = this.cycle,
            frequency = this.frequency,
            maxFitMate = this.maxFitMate,
            multiMediaEndPoints = this.multiMediaEndPoints,
        )
    }

    fun RegisterFitGroupResponseDto.toResponseRegisterFitGroup(): ResponseRegisterFitGroup {
        return ResponseRegisterFitGroup(
            isRegisterSuccess = this.isRegisterSuccess,
            fitGroupId = this.fitGroupId
        )
    }

    fun UpdateFitGroupResponseDto.toResponseUpdateFitGroup(): UpdateFitGroupResponse {
        return UpdateFitGroupResponse(
            isUpdateSuccess = this.isUpdateSuccess
        )
    }
}