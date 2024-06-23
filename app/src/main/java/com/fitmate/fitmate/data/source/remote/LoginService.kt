package com.fitmate.fitmate.data.source.remote

import com.fitmate.fitmate.data.model.dto.LoginResponse
import com.fitmate.fitmate.data.model.dto.LoginSuccessResponse
import com.fitmate.fitmate.data.model.dto.NicknameRequest
import com.fitmate.fitmate.data.model.dto.UserResponse
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Query

interface LoginService {

    @GET("auth/token/valid")
    suspend fun tokenValid(@Query("accessToken") accessToken: String): LoginSuccessResponse

    @GET("auth/login/naver")
    suspend fun loginNaver(@Query("code") code: String, @Query("fcmToken") token: String): LoginResponse

    @GET("auth/login/kakao")
    suspend fun loginKakao(@Query("code") code: String, @Query("fcmToken") token: String): LoginResponse

    @GET("auth/logout/naver")
    suspend fun logoutNaver(@Query("accessToken") accessToken: String): LoginSuccessResponse

    @GET("auth/logout/kakao")
    suspend fun logoutKakao(@Query("accessToken") accessToken: String): LoginSuccessResponse

    @GET("user/user-info")
    suspend fun getUserInfo(@Query("userId") userId: Int): UserResponse

    @PUT("uer/update/nickname")
    suspend fun updateNickname(@Body nicknameRequest: NicknameRequest): LoginSuccessResponse

    @DELETE("user/delete")
    suspend fun deleteUser(@Query("userId") userId: Int): LoginSuccessResponse
}