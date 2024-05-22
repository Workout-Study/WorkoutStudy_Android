package com.fitmate.fitmate.data.repository

import com.fitmate.fitmate.data.model.CertificationMapper.toCertificationRecordDto
import com.fitmate.fitmate.data.model.CertificationMapper.toResisterCertificationRecordDto
import com.fitmate.fitmate.data.model.dto.CertificationRecordResponseDto
import com.fitmate.fitmate.data.model.dto.CertificationTargetFitGroupResponseDto
import com.fitmate.fitmate.data.model.dto.ResisterCertificationRecordResponseDto
import com.fitmate.fitmate.data.source.remote.CertificationRecordService
import com.fitmate.fitmate.data.source.remote.CertificationTargetGroupService
import com.fitmate.fitmate.domain.model.DbCertification
import com.fitmate.fitmate.domain.model.ResisterCertificationRecord
import com.fitmate.fitmate.domain.repository.CertificationRecordRepository
import retrofit2.Response
import javax.inject.Inject

class CertificationRecordRepositoryImpl @Inject constructor(
    private val certificationRecordService: CertificationRecordService,
    private val certificationTargetGroupService: CertificationTargetGroupService
): CertificationRecordRepository {
    override suspend fun postCertificationRecord(requestRecordBody: DbCertification): Response<CertificationRecordResponseDto> {
        return certificationRecordService.postCertificationRecord(requestRecordBody.toCertificationRecordDto())
    }

    override suspend fun getMyFitGroupInfo(userId: Int): Response<CertificationTargetFitGroupResponseDto> {
        return certificationTargetGroupService.getTargetGroupList(userId)
    }

    override suspend fun postCertificationRecordToFitGroup(requestResisterCertificationRecordBody: ResisterCertificationRecord): Response<ResisterCertificationRecordResponseDto> {
        return certificationRecordService.postCertificationRecordToFitGroup(requestResisterCertificationRecordBody.toResisterCertificationRecordDto())
    }


}