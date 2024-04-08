package com.fitmate.fitmate.data.repository

import android.net.Uri
import android.util.Log
import com.fitmate.fitmate.data.model.CertificationMapper.toDbCertification
import com.fitmate.fitmate.data.model.CertificationMapper.toEntity
import com.fitmate.fitmate.data.source.dao.CertificationDao
import com.fitmate.fitmate.domain.model.DbCertification
import com.fitmate.fitmate.domain.repository.CertificationRepository
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.io.IOException
import javax.inject.Inject

class CertificationRepositoryImpl @Inject constructor(
    private val certificationDao: CertificationDao,
    private val storageRef: StorageReference,
) : CertificationRepository {
    override fun loadCertification(): Flow<List<DbCertification>> {
        return flow {
            certificationDao.selectAll().collect() { list ->
                emit(list.map { it.toDbCertification() })
            }
        }
    }

    override suspend fun selectOneById(id: Int): DbCertification? {
        return try {
            certificationDao.selectOne(id)?.toDbCertification()
        } catch (e: Exception) {
            Log.d("room", e.message.toString())
            null
        }
    }

    override suspend fun save(item: DbCertification): Boolean {
        return try {
            certificationDao.insert(item.toEntity())
            true
        } catch (e: Exception) {
            Log.d("room", "${e.message}")
            false
        }
    }

    override suspend fun update(item: DbCertification): Boolean {
        return try {
            certificationDao.insert(item.toEntity())
            true
        } catch (e: IOException) {
            Log.d("room", "${e.message}")
            false
        }
    }

    override suspend fun delete(): Boolean {
        return try {
            certificationDao.delete()
            true
        } catch (e: Exception) {
            Log.d("room", "${e.message}")
            false
        }
    }

    override suspend fun uploadImageToStorage(item: DbCertification): Map<String, MutableList<String>> =
        withContext(Dispatchers.IO) {
            val imageUrlMap = mutableMapOf<String, MutableList<String>>()

            runBlocking {
                // Start images 처리
                val startJob = launch {
                    item.startImages.mapIndexed { index, uri ->
                        async(Dispatchers.IO) {
                            val startFileName =
                                "${item.userId}_certificate_${item.recordStartDate}_start_$index"
                            if (imageUrlMap.containsKey("startUrls")) {
                                imageUrlMap["startUrls"]?.add(uploadImage(uri, startFileName))
                            } else {
                                val newList = mutableListOf(uploadImage(uri, startFileName))
                                imageUrlMap["startUrls"] = newList
                            }
                        }
                    }
                }

                val endJob = launch {
                    // End images 처리
                      item.endImages?.mapIndexed { index, uri ->
                        async(Dispatchers.IO) {
                            val endFileName =
                                "${item.userId}_certificate_${item.recordEndDate}_end_$index"
                            if (imageUrlMap.containsKey("endUrls")) {
                                imageUrlMap["endUrls"]?.add(uploadImage(uri, endFileName))
                            } else {
                                val newList = mutableListOf(uploadImage(uri, endFileName))
                                imageUrlMap["endUrls"] = newList
                            }
                        }
                    }
                }
                // 모든 작업이 완료될 때까지 대기
                startJob.join()
                endJob.join()
            }
            imageUrlMap
        }

    private suspend fun uploadImage(uri: Uri, fileName: String): String {
        storageRef.child(fileName).putFile(uri).await()

        // 업로드가 성공적으로 완료되었다면 다운로드 URL을 가져옴
        return storageRef.child(fileName).downloadUrl.await().toString()
    }
}