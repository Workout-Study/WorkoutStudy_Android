package com.fitmate.fitmate.data.source.dao

import com.fitmate.fitmate.data.model.dto.FitGroup
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface NetworkDao {
    @GET("retrieve/fit-group/")
    suspend fun retrieveFitGroup(@Query("fitMateId") fitMateId: Int): Response<List<FitGroup>>

    @GET("retrieve/message/{message_id}/{fit_group_id}/{fit_mate_id}/{message_time}/{message_type}")
    suspend fun retrieveMessage(
        @Path("message_id") messageId: String,
        @Path("fit_group_id") fitGroupId: String,
        @Path("fit_mate_id") fitMateId: String,
        @Path("message_time") messageTime: String,
        @Path("message_type") messageType: String
    ): List<String>
}