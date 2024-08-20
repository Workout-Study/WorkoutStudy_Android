package com.fitmate.fitmate.data.repository

import com.fitmate.fitmate.data.model.dto.LoginResponse
import com.fitmate.fitmate.data.model.dto.LoginSuccessResponse
import com.fitmate.fitmate.data.model.dto.NicknameRequest
import com.fitmate.fitmate.data.model.dto.UserResponse
import com.fitmate.fitmate.data.source.remote.LoginService
import com.fitmate.fitmate.domain.repository.LoginRepository

class LoginRepositoryImpl(private val loginService: LoginService): LoginRepository {
    override suspend fun tokenValid(accessToken: String): LoginSuccessResponse = loginService.tokenValid(accessToken)

    override suspend fun loginNaver(code: String, token: String, imageUrl: String): LoginResponse = loginService.loginNaver(code, token, imageUrl)

    override suspend fun loginKakao(code: String, token: String, imageUrl: String): LoginResponse = loginService.loginKakao(code, token, imageUrl)

    override suspend fun logoutNaver(accessToken: String): LoginSuccessResponse = loginService.logoutNaver(accessToken)

    override suspend fun logoutKakao(accessToken: String): LoginSuccessResponse = loginService.logoutKakao(accessToken)

    override suspend fun getUserInfo(userId: Int): UserResponse = loginService.getUserInfo(userId)

    override suspend fun updateNickname(userId: String, nickname: String, imageUrl: String?): LoginSuccessResponse = loginService.updateNickname(NicknameRequest(nickname, imageUrl), userId)

    override suspend fun deleteUser(userId: Int): LoginSuccessResponse = loginService.deleteUser(userId)
}