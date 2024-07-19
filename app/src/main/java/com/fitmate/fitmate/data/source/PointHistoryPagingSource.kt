package com.fitmate.fitmate.data.source

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.fitmate.fitmate.data.model.dto.FitGroupDetail
import com.fitmate.fitmate.data.model.dto.PointHistoryContentDto
import com.fitmate.fitmate.data.model.dto.PointHistoryDto
import com.fitmate.fitmate.data.source.remote.GroupService
import com.fitmate.fitmate.data.source.remote.PointService
import retrofit2.http.Query

class PointHistoryPagingSource(
    private val pointService: PointService,
    private val withMaxGroup: Int,
    private val pointOwnerType: String,
    private val historyStartDate: String,
    private val historyEndDate: String,
    private val pageNumber: Int,
    private val pageSize: Int,
    private val tradeType: String
): PagingSource<Int, PointHistoryContentDto>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, PointHistoryContentDto> {
        return try {
            val page = params.key ?: 0
            val response = pointService.getPointHistory(
                withMaxGroup,
                pointOwnerType,
                historyStartDate,
                historyEndDate,
                pageNumber,
                pageSize,
                tradeType)

            val prevKey = if (response.pageNumber == 0) null else page - 1
            val nextKey = if (!response.hasNext) null else page + 1

            LoadResult.Page(
                data = response.content,
                prevKey = prevKey,
                nextKey = nextKey
            )
        }catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, PointHistoryContentDto>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

}