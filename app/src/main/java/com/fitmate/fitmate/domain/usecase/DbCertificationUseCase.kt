package com.fitmate.fitmate.domain.usecase

import com.fitmate.fitmate.domain.model.DbCertification
import com.fitmate.fitmate.domain.repository.CertificationRepository
import javax.inject.Inject

class DbCertificationUseCase @Inject constructor(
    private val certificationRepository: CertificationRepository,
) {
    fun loadList() = certificationRepository.loadCertification()

    suspend fun getItemById(id: Int) = certificationRepository.selectOneById(id)

    suspend fun save(item: DbCertification) = certificationRepository.save(item)

    suspend fun update(item: DbCertification) = certificationRepository.update(item)

    suspend fun delete() = certificationRepository.delete()

    suspend fun uploadAndGetImageUrl(item: DbCertification) = certificationRepository.uploadImageToStorage(item)
}