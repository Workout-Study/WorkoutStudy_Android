package com.fitmate.fitmate.domain.repository

import android.net.Uri

interface MakeFitGroupRepository {
    suspend fun uploadGroupImageToStorage(userId:String, groupName:String, groupImageList:List<Uri>): List<String>
}