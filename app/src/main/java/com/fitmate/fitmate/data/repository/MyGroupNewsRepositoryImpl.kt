package com.fitmate.fitmate.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.fitmate.fitmate.data.source.MyGroupNewsPagingSource
import com.fitmate.fitmate.data.source.remote.MyGroupNewsService
import com.fitmate.fitmate.domain.repository.MyGroupNewsRepository

class MyGroupNewsRepositoryImpl(private val myGroupNewsService: MyGroupNewsService) :
    MyGroupNewsRepository {
    override suspend fun getMyGroupNews(
        userId: Int,
        pageNumber: Int,
        pageSize: Int,
    ) = Pager(
        config = PagingConfig(
            pageSize,
            enablePlaceholders = true
        ),
        pagingSourceFactory = {
            MyGroupNewsPagingSource(
                myGroupNewsService,
                userId,
                pageNumber,
                pageSize,
                )
        }
    ).flow
}