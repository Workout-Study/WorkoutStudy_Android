package com.fitmate.fitmate.domain.repository

import com.fitmate.fitmate.data.model.dto.GroupDetailResponse
import com.fitmate.fitmate.data.model.dto.GroupResponse

interface GroupRepository {
    suspend fun getFilteredFitGroups(withMaxGroup: Boolean, category: Int, pageNumber: Int, pageSize: Int): GroupResponse

    suspend fun getFitGroupDetail(fitGroupId: Int): GroupDetailResponse
}