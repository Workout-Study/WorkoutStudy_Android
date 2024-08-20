package com.fitmate.fitmate.data.model.dto

import com.google.gson.annotations.SerializedName
import java.io.Serializable

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
    val imageUrl: String?
)

data class UserResponse(
    @SerializedName("userId") val userId: Int,
    @SerializedName("nickname") val nickname: String,
    @SerializedName("imageUrl") val imageUrl: String? = null,
    @SerializedName("state") val state: Boolean,
    @SerializedName("createdAt") val createdAt: String,
    @SerializedName("createdBy") val createdBy: String,
): Serializable {
    override fun hashCode(): Int {
        var result = userId ?: 0
        result = 31 * result + (nickname?.hashCode() ?: 0)
        result = 31 * result + (imageUrl?.hashCode() ?: 0)
        result = 31 * result + (state?.hashCode() ?: 0)
        result = 31 * result + (createdAt?.hashCode() ?: 0)
        result = 31 * result + (createdBy?.hashCode() ?: 0)
        return result
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as UserResponse

        if (userId != other.userId) return false
        if (nickname != other.nickname) return false
        if (imageUrl != other.imageUrl) return false
        if (state != other.state) return false
        if (createdAt != other.createdAt) return false
        if (createdBy != other.createdBy) return false

        return true
    }
}