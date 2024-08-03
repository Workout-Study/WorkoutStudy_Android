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
    @SerializedName("isNewUser") val isNewUser: Int,
    @SerializedName("fcmToken") val token: String,
    @SerializedName("imageUrl") val imageUrl: String,
    @SerializedName("createdAt") val createdAt: String
)

data class NicknameRequest(
    val nickname: String,
    val imageUrl: String
)

data class UserResponse(
    @SerializedName("userId") val userId: Int,
    @SerializedName("nickname") val nickname: String,
    @SerializedName("imageUrl") val imageUrl: String,
    @SerializedName("state") val state: Boolean,
    @SerializedName("createdAt") val createdAt: String,
    @SerializedName("createdBy") val createdBy: String,
)