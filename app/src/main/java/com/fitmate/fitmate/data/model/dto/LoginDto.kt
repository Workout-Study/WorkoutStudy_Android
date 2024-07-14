package com.fitmate.fitmate.data.model.dto

import com.google.gson.annotations.SerializedName

data class LoginSuccessResponse(
    @SerializedName("code") val code: Int,
    @SerializedName("message") val message: String
)

data class LoginResponse(
    @SerializedName("resultCode") val resultCode: String,
    @SerializedName("accessToken") val accessToken: String,
    @SerializedName("refreshToken") val refreshToken: String,
    @SerializedName("userId") val userId: Int,
    @SerializedName("isNewUser") val isNewUser: Int
)

data class NicknameRequest(
    val nickname: String
)

data class UserResponse(
    @SerializedName("userId") val userId: Int,
    @SerializedName("nickname") val nickname: String,
    @SerializedName("createdAt") val createdAt: String,
    @SerializedName("createdBy") val createdBy: String,
)