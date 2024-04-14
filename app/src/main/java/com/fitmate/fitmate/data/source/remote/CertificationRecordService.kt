package com.fitmate.fitmate.data.source.remote

import com.fitmate.fitmate.data.model.dto.CertificationRecordDto
import com.fitmate.fitmate.data.model.dto.CertificationRecordResponseDto
import com.fitmate.fitmate.data.model.dto.ResisterCertificationRecordDto
import com.fitmate.fitmate.data.model.dto.ResisterCertificationRecordResponseDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface CertificationRecordService {
    /*@POST("my-fit-service/records")*/
    @POST("records")

    suspend fun postCertificationRecord(@Body requestBody: CertificationRecordDto): Response<CertificationRecordResponseDto>
    /*@POST("my-fit-service/certifications")*/
    @POST("mates")
    suspend fun postCertificationRecordToFitGroup(@Body requestBody: ResisterCertificationRecordDto): Response<ResisterCertificationRecordResponseDto>


}