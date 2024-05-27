package com.fitmate.fitmate.domain.repository

import android.net.Uri
import com.fitmate.fitmate.data.model.dto.RegisterFitGroupResponseDto
import com.fitmate.fitmate.data.model.dto.RequestRegisterFitGroupBodyDto
import com.fitmate.fitmate.domain.model.RequestRegisterFitGroupBody
import retrofit2.Response

interface MakeFitGroupRepository {
    suspend fun uploadGroupImageToStorage(userId:String, groupName:String, groupImageList:List<Uri>): List<String>

    suspend fun postRegisterFitGroup(registerGroupItem: RequestRegisterFitGroupBodyDto): Response<RegisterFitGroupResponseDto>
}