package com.fitmate.fitmate.data.model

import com.fitmate.fitmate.data.model.dto.FitRecordHistoryResponse
import com.fitmate.fitmate.data.model.dto.MyFitProgressResponseDto
import com.fitmate.fitmate.domain.model.FitProgressItem
import com.fitmate.fitmate.domain.model.MyFitRecordHistory
import com.fitmate.fitmate.domain.model.MyFitRecordHistoryDetail
import com.fitmate.fitmate.domain.model.MyFitRecordHistoryGroupInfo

object MyFitMapper {

    fun MyFitProgressResponseDto.toMyFitProgressResponse(): List<FitProgressItem> {
        return this.myFitProgressResponse.map {
            FitProgressItem(
                itemId = it.fitGroupId,
                itemName = it.fitGroupName,
                thumbnailEndPoint = it.thumbnailEndPoint,
                cycle = it.cycle,
                frequency = it.frequency,
                certificationCount = it.certificationCount
            )
        }
    }

    fun FitRecordHistoryResponse.mapToMyFitRecordHistory(): MyFitRecordHistory {
        val myFitRecordHistoryDetailList = this.fitRecordDetailResponseDtoList.map { fitRecordHistoryDetail ->
            MyFitRecordHistoryDetail(
                fitRecordId = fitRecordHistoryDetail.fitRecordId,
                recordStartDate = fitRecordHistoryDetail.recordStartDate,
                recordEndDate = fitRecordHistoryDetail.recordEndDate,
                createdAt = fitRecordHistoryDetail.createdAt,
                multiMediaEndPoints = fitRecordHistoryDetail.multiMediaEndPoints,
                registeredFitCertifications = fitRecordHistoryDetail.registeredFitCertifications.map { fitRecordHistoryGroupInfo ->
                    MyFitRecordHistoryGroupInfo(
                        fitGroupId = fitRecordHistoryGroupInfo.fitGroupId,
                        fitGroupName = fitRecordHistoryGroupInfo.fitGroupName,
                        fitCertificationId = fitRecordHistoryGroupInfo.fitCertificationId,
                        certificationStatus = fitRecordHistoryGroupInfo.certificationStatus,
                        createdAt = fitRecordHistoryGroupInfo.createdAt,
                        voteEndDate = fitRecordHistoryGroupInfo.voteEndDate
                    )
                }
            )
        }

        return MyFitRecordHistory(myFitRecordHistoryDetailList)
    }
}