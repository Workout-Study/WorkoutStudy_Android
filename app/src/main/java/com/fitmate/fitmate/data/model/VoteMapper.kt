package com.fitmate.fitmate.data.model

import com.fitmate.fitmate.data.model.dto.EachVoteCertificationResponseDto
import com.fitmate.fitmate.data.model.dto.GroupVoteCertificationDetailDto
import com.fitmate.fitmate.data.model.dto.VoteRequestDto
import com.fitmate.fitmate.data.model.dto.VoteResponseDto
import com.fitmate.fitmate.data.model.dto.VoteUpdateResponseDto
import com.fitmate.fitmate.domain.model.EachVoteCertificationResponse
import com.fitmate.fitmate.domain.model.GroupVoteCertificationDetail
import com.fitmate.fitmate.domain.model.VoteRequest
import com.fitmate.fitmate.domain.model.VoteResponse
import com.fitmate.fitmate.domain.model.VoteUpdateResponse

object VoteMapper {
    fun mapEachVoteCertificationResponseDto(dto: EachVoteCertificationResponseDto): EachVoteCertificationResponse {
        val details = dto.fitCertificationDetails.map { detailDto ->
            GroupVoteCertificationDetail(
                certificationId = detailDto.certificationId,
                recordId = detailDto.recordId,
                certificationRequestUserId = detailDto.certificationRequestUserId,
                certificationRequestUserNickname = detailDto.certificationRequestUserNickname,
                isUserVoteDone = detailDto.isUserVoteDone,
                isUserAgree = detailDto.isUserAgree,
                agreeCount = detailDto.agreeCount,
                disagreeCount = detailDto.disagreeCount,
                maxAgreeCount = detailDto.maxAgreeCount,
                multiMediaEndPoints = detailDto.multiMediaEndPoints,
                fitRecordStartDate = detailDto.fitRecordStartDate,
                fitRecordEndDate = detailDto.fitRecordEndDate,
                voteEndDate = detailDto.voteEndDate
            )
        }
        return EachVoteCertificationResponse(details)
    }

    fun VoteRequest.toVoteRequestDto(): VoteRequestDto {
        return VoteRequestDto(
            requestUserId,
            agree,
            targetCategory,
            targetId
        )
    }

    fun VoteResponseDto.VoteResponse(): VoteResponse {
        return VoteResponse(
            isRegisterSuccess
        )
    }

    fun VoteUpdateResponseDto.VoteResponse(): VoteUpdateResponse {
        return VoteUpdateResponse(
            isUpdateSuccess
        )
    }
}