package com.fitmate.fitmate.data.source.remote

import com.fitmate.fitmate.data.model.dto.PointDto
import com.fitmate.fitmate.data.model.dto.PointHistoryDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface PointService {
    @GET("/wallet-service/wallets/{wallet-owner-id}/{wallet-owner-type}")
    suspend fun getPointInfo(
        @Path("wallet-owner-id") pointOwnerId: Int,
        @Path("wallet-owner-type") pointOwnerType: String,
    ): Response<PointDto>

    @GET("/wallet-service/wallets/history")
    suspend fun getPointHistory(
        @Query("walletOwnerId") pointOwnerId: Int,
        @Query("walletOwnerType") pointOwnerType: String,
        @Query("historyStartDate") historyStartDate: String?,
        @Query("historyEndDate") historyEndDate: String?,
        @Query("pageNumber") pageNumber: Int,
        @Query("pageSize") pageSize: Int,
        @Query("tradeType") tradeType: String?,
    ):PointHistoryDto
}