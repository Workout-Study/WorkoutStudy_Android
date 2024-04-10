package com.fitmate.fitmate.domain.usecase

import com.fitmate.fitmate.data.model.dto.CertificationRecordResponse
import com.fitmate.fitmate.domain.model.DbCertification
import com.fitmate.fitmate.domain.repository.CertificationRecordRepository
import retrofit2.Response
import javax.inject.Inject

class CertificationRecordNetworkUseCase @Inject constructor(
    private val certificationRecordRepository: CertificationRecordRepository
) {
    suspend fun postCertificationRecord(item:DbCertification):Response<CertificationRecordResponse> = certificationRecordRepository.postCertificationRecord(item)
}