package com.fitmate.fitmate.domain.repository

import com.fitmate.fitmate.data.model.dto.LoginResponse
import com.fitmate.fitmate.data.model.dto.LoginSuccessResponse
import com.fitmate.fitmate.data.model.dto.UserResponse

interface LoginRepository {
    suspend fun tokenValid(accessToken: String): LoginSuccessResponse

    suspend fun loginNaver(code: String): LoginResponse

    suspend fun loginKakao(code: String): LoginResponse

    suspend fun logoutNaver(accessToken: String): LoginSuccessResponse

    suspend fun logoutKakao(accessToken: String): LoginSuccessResponse

    suspend fun getUserInfo(userId: Int): UserResponse

    suspend fun updateNickname(userId: Int, nickname: String): LoginSuccessResponse

    suspend fun deleteUser(userId: Int): LoginSuccessResponse
}
