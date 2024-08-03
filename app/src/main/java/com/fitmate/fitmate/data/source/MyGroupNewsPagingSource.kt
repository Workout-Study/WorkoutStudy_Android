package com.fitmate.fitmate.data.source

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.fitmate.fitmate.data.model.dto.MyGroupNewsDto
import com.fitmate.fitmate.data.model.dto.MyGroupNewsResponseDto
import com.fitmate.fitmate.data.source.remote.MyGroupNewsService

class MyGroupNewsPagingSource(
    private val myGroupNewsService: MyGroupNewsService,
    private val userId: Int,
    private val pageNumber: Int,
    private val pageSize: Int,
): PagingSource<Int, MyGroupNewsDto>()  {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, MyGroupNewsDto> {
        return try {
            val page = params.key ?: 0
            val response = myGroupNewsService.getGroupNews(
                userId,
                pageNumber,
                pageSize)

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

    override fun getRefreshKey(state: PagingState<Int, MyGroupNewsDto>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }
}