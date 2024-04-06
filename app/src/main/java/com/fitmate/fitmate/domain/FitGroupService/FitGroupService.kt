package com.fitmate.fitmate.domain.FitGroupService

import retrofit2.http.GET
import retrofit2.http.Path

interface FitGroupService {
    @GET("retrieve/fit-group/{fit_mate_id}")
    fun getFitGroup(@Path("fit_mate_id") fitMateId: Int): List<FitGroup>
}

data class FitGroup(
    val iD: Int,
    val fitGroupName: String,
    val maxFitMate: Int,
    val createdAt: String,
    val createdBy: String,
    val updatedAt: String,
    val updatedBy: String
)
