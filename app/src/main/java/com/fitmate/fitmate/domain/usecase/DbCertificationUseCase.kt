package com.fitmate.fitmate.domain.usecase

import com.fitmate.fitmate.domain.model.DbCertification
import com.fitmate.fitmate.domain.repository.CertificationRepository
import javax.inject.Inject

class DbCertificationUseCase @Inject constructor(
    private val certificationRepository: CertificationRepository
){
    fun loadList() = certificationRepository.loadCertification()

    suspend fun save(item: DbCertification) = certificationRepository.save(item)

    suspend fun delete() = certificationRepository.delete()
}