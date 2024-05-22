package com.fitmate.fitmate.domain.usecase

import android.net.Uri
import com.fitmate.fitmate.data.model.MakeFitGroupMapper.toRegisterFitGroupDto
import com.fitmate.fitmate.data.model.dto.RegisterFitGroupResponseDto
import com.fitmate.fitmate.data.model.dto.RequestRegisterFitGroupBodyDto
import com.fitmate.fitmate.domain.model.RequestRegisterFitGroupBody
import com.fitmate.fitmate.domain.repository.MakeFitGroupRepository
import retrofit2.Response
import javax.inject.Inject

class MakeFitGroupUseCase @Inject constructor(
    private val makeFitGroupRepository: MakeFitGroupRepository
){
    suspend fun uploadGroupImageAndGetUrl(userId:String, groupName:String, groupImageList:List<Uri>): List<String> = makeFitGroupRepository.uploadGroupImageToStorage(userId, groupName, groupImageList)

    suspend fun postRegisterFitGroup(fitGroupInfo: RequestRegisterFitGroupBodyDto): Response<RegisterFitGroupResponseDto> = makeFitGroupRepository.postRegisterFitGroup(fitGroupInfo)
}