package com.fitmate.fitmate.domain.usecase

import android.net.Uri
import com.fitmate.fitmate.domain.repository.MakeFitGroupRepository
import javax.inject.Inject

class MakeFitGroupUseCase @Inject constructor(
    private val makeFitGroupRepository: MakeFitGroupRepository
){
    suspend fun uploadGroupImageAndGetUrl(userId:String, groupName:String, groupImageList:List<Uri>): List<String> = makeFitGroupRepository.uploadGroupImageToStorage(userId, groupName, groupImageList)
}