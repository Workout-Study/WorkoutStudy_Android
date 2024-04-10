package com.fitmate.fitmate.data.repository

import com.fitmate.fitmate.data.model.CertificationMapper.toCertificationRecordDto
import com.fitmate.fitmate.data.model.dto.CertificationRecordResponse
import com.fitmate.fitmate.data.source.remote.CertificationRecordService
import com.fitmate.fitmate.domain.model.CertificationRecord
import com.fitmate.fitmate.domain.model.DbCertification
import com.fitmate.fitmate.domain.repository.CertificationRecordRepository
import com.fitmate.fitmate.domain.repository.NetworkRepository
import retrofit2.Response
import javax.inject.Inject

class CertificationRecordRepositoryImpl @Inject constructor(
    private val certificationRecordService: CertificationRecordService
): CertificationRecordRepository {
    override suspend fun postCertificationRecord(requestRecordBody: DbCertification): Response<CertificationRecordResponse> {
        return certificationRecordService.postCertificationRecord(requestRecordBody.toCertificationRecordDto())
    }

}