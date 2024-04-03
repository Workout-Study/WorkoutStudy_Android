package com.fitmate.fitmate.data.repository

import android.util.Log
import com.fitmate.fitmate.data.model.CertificationMapper.toDbCertification
import com.fitmate.fitmate.data.model.CertificationMapper.toEntity
import com.fitmate.fitmate.data.source.dao.CertificationDao
import com.fitmate.fitmate.domain.model.DbCertification
import com.fitmate.fitmate.domain.repository.CertificationRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.IOException
import javax.inject.Inject

class CertificationRepositoryImpl @Inject constructor(
    private val certificationDao: CertificationDao
): CertificationRepository {
    override fun loadCertification(): Flow<List<DbCertification>> {
        return flow{
            certificationDao.selectAll().collect(){list->
                emit(list.map { it.toDbCertification() })
            }
        }
    }

    override suspend fun selectOneById(id: Int): DbCertification? {
        return try {
            certificationDao.selectOne(id)?.toDbCertification()
        }catch (e: Exception) {
            Log.d("room",e.message.toString())
            null
        }
    }

    override suspend fun save(item: DbCertification): Boolean {
        return try {
            certificationDao.insert(item.toEntity())
            true
        }catch (e: Exception){
            Log.d("room","${e.message}")
            false
        }
    }

    override suspend fun update(item: DbCertification): Boolean {
        return try{
            certificationDao.insert(item.toEntity())
            true
        }catch (e:IOException){
            Log.d("room","${e.message}")
            false
        }
    }

    override suspend fun delete(): Boolean {
        return try {
            certificationDao.delete()
            true
        }catch (e:Exception){
            Log.d("room","${e.message}")
            false
        }
    }

}