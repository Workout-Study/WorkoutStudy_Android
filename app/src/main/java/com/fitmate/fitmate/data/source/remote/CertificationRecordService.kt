package com.fitmate.fitmate.data.source.remote

import com.fitmate.fitmate.data.model.dto.CertificationRecordDto
import com.fitmate.fitmate.data.model.dto.CertificationRecordResponseDto
import com.fitmate.fitmate.data.model.dto.ResisterCertificationRecordDto
import com.fitmate.fitmate.data.model.dto.ResisterCertificationRecordResponseDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface CertificationRecordService {

    //@POST("records")
    @POST("my-fit-service/records")
    suspend fun postCertificationRecord(@Body requestBody: CertificationRecordDto): Response<CertificationRecordResponseDto>

    //@POST("mates")
    @POST("my-fit-service/certifications")
    suspend fun postCertificationRecordToFitGroup(@Body requestBody: ResisterCertificationRecordDto): Response<ResisterCertificationRecordResponseDto>


}