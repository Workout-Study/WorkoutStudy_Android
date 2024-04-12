package com.fitmate.fitmate.domain.repository

import com.fitmate.fitmate.data.model.dto.CertificationRecordResponseDto
import com.fitmate.fitmate.data.model.dto.CertificationTargetFitGroupResponseDto
import com.fitmate.fitmate.domain.model.DbCertification
import retrofit2.Response

interface CertificationRecordRepository {
    suspend fun postCertificationRecord(requestRecordBody:DbCertification): Response<CertificationRecordResponseDto>

    suspend fun getMyFitGroupInfo(userId:String):Response<CertificationTargetFitGroupResponseDto>
}