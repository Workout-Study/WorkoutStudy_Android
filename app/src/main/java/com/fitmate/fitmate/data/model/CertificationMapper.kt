package com.fitmate.fitmate.data.model

import android.net.Uri
import com.fitmate.fitmate.data.model.dto.*
import com.fitmate.fitmate.data.model.entity.CertificationEntity
import com.fitmate.fitmate.domain.model.CertificationRecordResponse
import com.fitmate.fitmate.domain.model.DbCertification
import com.fitmate.fitmate.domain.model.FitGroupItem
import com.fitmate.fitmate.domain.model.ResisterCertificationRecord
import com.fitmate.fitmate.domain.model.ResisterCertificationRecordResponse

object CertificationMapper {

    //ui layer의 데이터를 data layer의 엔티티로 변환
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

    //data layer의 엔티티를 ui layer의 데이터로 변환
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


    //ui layer의 데이터를 data layer의 엔티티로 변환
    fun DbCertification.toCertificationRecordDto() = CertificationRecordDto(
        requestUserId = this.userId,
        recordStartDate = this.recordStartDate.toString(),
        recordEndDate = this.recordEndDate.toString(),
        multiMediaEndPoints = (this.startImagesUrl!! + this.endImagesUrl!!).toList()
    )

    //data layer의 엔티티를 ui layer의 데이터로 변환
    fun CertificationRecordResponseDto.toCertificationRecordResponse() = CertificationRecordResponse(
        isRegisterSuccess = this.isRegisterSuccess,
        fitRecordId = this.fitRecordId
    )


    fun CertificationTargetFitGroupResponseDto.toCertificationTargetFitGroupResponse(): List<FitGroupItem> {
        return this.fitGroupItems.map {
            FitGroupItem(
                fitGroupId = it.fitGroupId,
                fitGroupName = it.fitGroupName
            )
        }
    }

    fun ResisterCertificationRecord.toResisterCertificationRecordDto() = ResisterCertificationRecordDto(
        requestUserId = this.requestUserId,
        fitRecordId = this.fitRecordId,
        fitGroupIds = this.fitGroupIds
    )

    fun ResisterCertificationRecordResponseDto.toResisterCertificationRecordResponse() = ResisterCertificationRecordResponse(
        isRegisterSuccess = this.isRegisterSuccess,
    )
}