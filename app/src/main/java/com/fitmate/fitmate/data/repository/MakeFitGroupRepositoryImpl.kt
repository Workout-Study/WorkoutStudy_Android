package com.fitmate.fitmate.data.repository

import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import com.fitmate.fitmate.data.model.MakeFitGroupMapper.toRegisterFitGroupDto
import com.fitmate.fitmate.data.model.dto.RegisterFitGroupResponseDto
import com.fitmate.fitmate.data.model.dto.RequestRegisterFitGroupBodyDto
import com.fitmate.fitmate.data.source.remote.RegisterFitGroupService
import com.fitmate.fitmate.domain.model.RequestRegisterFitGroupBody
import com.fitmate.fitmate.domain.repository.MakeFitGroupRepository
import com.google.firebase.storage.StorageReference
import id.zelory.compressor.Compressor
import id.zelory.compressor.constraint.quality
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import retrofit2.Response
import java.io.File
import javax.inject.Inject

class MakeFitGroupRepositoryImpl @Inject constructor(
    private val registerFitGroupService: RegisterFitGroupService,
    private val storageRef: StorageReference,
    private val context: Context,
) : MakeFitGroupRepository {

    override suspend fun uploadGroupImageToStorage(
        userId: String,
        groupName: String,
        groupImageList: List<Uri>,
    ): List<String> {
        return withContext(Dispatchers.IO) {
            val imageUrlList = mutableListOf<String>()

            // Start images 처리
            val job = groupImageList.mapIndexed { index, uri ->
                async {
                    val fileName =
                        "${userId}_${groupName}_$index"
                    imageUrlList.add(uploadImage(uri, fileName))
                }
            }

            // 이미지 업로드 작업들이 완료될 때까지 대기
            job.awaitAll()

            return@withContext imageUrlList
        }
    }

    private suspend fun uploadImage(uri: Uri, fileName: String): String {
        val file = getPathFromURI(uri)
        try {
            val compressorFile = Compressor.compress(context, File(file)) {
                quality(0)
            }
            storageRef.child("group_profile/").child(fileName).putFile(Uri.fromFile(compressorFile)).await()
            // 업로드가 성공적으로 완료되었다면 다운로드 URL을 가져옴
            return storageRef.child("group_profile/").child(fileName).downloadUrl.await().toString()
        }catch (e:FileAlreadyExistsException){
            storageRef.child("group_profile/").child(fileName).putFile(uri).await()
            // 업로드가 성공적으로 완료되었다면 다운로드 URL을 가져옴
            return storageRef.child("group_profile/").child(fileName).downloadUrl.await().toString()
        }
    }

    private fun getPathFromURI(uri: Uri): String? {
        var path: String? = null
        val projection = arrayOf(MediaStore.Images.Media.DATA)

        context.contentResolver.query(uri, projection, null, null, null)?.use { cursor ->
            cursor.moveToFirst()
            path = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA))
        }
        return path
    }

    override suspend fun postRegisterFitGroup(registerGroupItem: RequestRegisterFitGroupBodyDto): Response<RegisterFitGroupResponseDto> {
        return registerFitGroupService.registerFitGroup(registerGroupItem)
    }
}