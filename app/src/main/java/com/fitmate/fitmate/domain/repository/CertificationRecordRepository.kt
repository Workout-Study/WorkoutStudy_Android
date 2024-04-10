package com.fitmate.fitmate.domain.repository

import com.fitmate.fitmate.data.model.dto.CertificationRecordResponse
import com.fitmate.fitmate.domain.model.DbCertification
import retrofit2.Response

interface CertificationRecordRepository {
    suspend fun postCertificationRecord(requestRecordBody:DbCertification): Response<CertificationRecordResponse>
}