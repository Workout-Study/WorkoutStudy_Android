package com.fitmate.fitmate.domain.usecase

import com.fitmate.fitmate.data.model.dto.CertificationRecordResponseDto
import com.fitmate.fitmate.data.model.dto.CertificationTargetFitGroupResponseDto
import com.fitmate.fitmate.domain.model.DbCertification
import com.fitmate.fitmate.domain.model.ResisterCertificationRecord
import com.fitmate.fitmate.domain.repository.CertificationRecordRepository
import retrofit2.Response
import javax.inject.Inject

class CertificationRecordNetworkUseCase @Inject constructor(
    private val certificationRecordRepository: CertificationRecordRepository
) {
    suspend fun postCertificationRecord(item:DbCertification):Response<CertificationRecordResponseDto> = certificationRecordRepository.postCertificationRecord(item)

    suspend fun getMyFitGroup(userId:String):Response<CertificationTargetFitGroupResponseDto> = certificationRecordRepository.getMyFitGroupInfo(userId)

    suspend fun postResisterCertificationRecordToFitGroup(item:ResisterCertificationRecord) = certificationRecordRepository.postCertificationRecordToFitGroup(item)
}