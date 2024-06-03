package com.fitmate.fitmate.data.source

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.fitmate.fitmate.data.model.dto.FitGroupDetail
import com.fitmate.fitmate.data.source.remote.GroupService

class CategoryPagingSource(
    private val groupService: GroupService,
    private val categoryNum: Int,
    private val pageSize: Int
): PagingSource<Int, FitGroupDetail>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, FitGroupDetail> {
        return try {
            val page = params.key ?: 0
            val response = groupService.fitGroupFilter(true, categoryNum, page, pageSize)
            val prevKey = if (response.first) null else page - 1
            val nextKey = if (response.last) null else page + 1

            LoadResult.Page(
                data = response.content,
                prevKey = prevKey,
                nextKey = nextKey
            )
        }catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, FitGroupDetail>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }
}