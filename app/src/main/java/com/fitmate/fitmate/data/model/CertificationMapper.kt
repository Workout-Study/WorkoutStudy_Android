package com.fitmate.fitmate.data.model

import android.net.Uri
import com.fitmate.fitmate.data.model.dto.CertificationRecordDto
import com.fitmate.fitmate.data.model.entity.CertificationEntity
import com.fitmate.fitmate.data.model.entity.ChatEntity
import com.fitmate.fitmate.domain.model.CertificationRecord
import com.fitmate.fitmate.domain.model.ChatItem
import com.fitmate.fitmate.domain.model.DbCertification

object CertificationMapper {

    fun DbCertification.toEntity() = CertificationEntity(
        id = id,
        userId = userId,
        recordStartDate = recordStartDate,
        recordEndDate = recordEndDate,
        startImages = startImages,
        endImages = endImages ?: emptyList<Uri>().toMutableList(),
        certificateTime = certificateTime,
        startImagesUrl = startImagesUrl ?: emptyList<String>().toMutableList(),
        endImagesUrl = endImagesUrl ?: emptyList<String>().toMutableList(),
    )

    fun CertificationEntity.toDbCertification() = DbCertification(
        id = id,
        userId = userId,
        recordStartDate = recordStartDate,
        recordEndDate = recordEndDate,
        startImages = startImages,
        endImages = endImages,
        certificateTime = certificateTime,
        startImagesUrl = startImagesUrl,
        endImagesUrl = endImagesUrl
    )

    fun DbCertification.toCertificationRecordDto() = CertificationRecordDto(
        requestUserId = this.userId,
        recordStartDate = this.recordStartDate.toString(),
        recordEndDate = this.recordEndDate.toString(),
        multiMediaEndPoints = (this.startImagesUrl!! + this.endImagesUrl!!).toList()
    )
}