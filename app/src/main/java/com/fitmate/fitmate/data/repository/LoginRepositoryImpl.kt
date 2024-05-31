package com.fitmate.fitmate.data.repository

import com.fitmate.fitmate.data.model.dto.LoginResponse
import com.fitmate.fitmate.data.model.dto.LoginSuccessResponse
import com.fitmate.fitmate.data.model.dto.NicknameRequest
import com.fitmate.fitmate.data.model.dto.UserResponse
import com.fitmate.fitmate.data.source.remote.LoginService
import com.fitmate.fitmate.domain.repository.LoginRepository

class LoginRepositoryImpl(private val loginService: LoginService): LoginRepository {
    override suspend fun tokenValid(accessToken: String): LoginSuccessResponse = loginService.tokenValid(accessToken)

    override suspend fun loginNaver(code: String): LoginResponse = loginService.loginNaver(code)

    override suspend fun loginKakao(code: String): LoginResponse = loginService.loginKakao(code)

    override suspend fun logoutNaver(accessToken: String): LoginSuccessResponse = loginService.logoutNaver(accessToken)

    override suspend fun logoutKakao(accessToken: String): LoginSuccessResponse = loginService.logoutKakao(accessToken)

    override suspend fun getUserInfo(userId: Int): UserResponse = loginService.getUserInfo(userId)

    override suspend fun updateNickname(userId: Int, nickname: String): LoginSuccessResponse = loginService.updateNickname(NicknameRequest(userId, nickname))

    override suspend fun deleteUser(userId: Int): LoginSuccessResponse = loginService.deleteUser(userId)
}