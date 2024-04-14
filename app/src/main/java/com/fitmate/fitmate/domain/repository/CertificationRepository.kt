package com.fitmate.fitmate.domain.repository

import android.net.Uri
import com.fitmate.fitmate.domain.model.DbCertification
import kotlinx.coroutines.flow.Flow

interface CertificationRepository {

    fun loadCertification(): Flow<List<DbCertification>>

    suspend fun selectOneById(id: Int): DbCertification?

    suspend fun save(item: DbCertification): Boolean

    suspend fun update(item: DbCertification): Boolean

    suspend fun delete(): Boolean

    suspend fun uploadImageToStorage(item: DbCertification): Map<String,MutableList<String>>
}