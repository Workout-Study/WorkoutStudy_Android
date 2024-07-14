package com.fitmate.fitmate.domain.usecase

import com.fitmate.fitmate.domain.repository.LoginRepository
import javax.inject.Inject

class LoginUseCase @Inject constructor(private val loginRepository: LoginRepository) {

    suspend fun tokenValid(accessToken: String) = loginRepository.tokenValid(accessToken)

    suspend fun loginNaver(code: String, token: String) = loginRepository.loginNaver(code, token)

    suspend fun loginKakao(code: String, token: String) = loginRepository.loginKakao(code, token)

    suspend fun logoutNaver(accessToken: String) = loginRepository.logoutNaver(accessToken)

    suspend fun logoutKakao(accessToken: String) = loginRepository.logoutKakao(accessToken)

    suspend fun getUserInfo(userId: Int) = loginRepository.getUserInfo(userId)

    suspend fun updateNickname(userId: String, nickname: String) = loginRepository.updateNickname(userId, nickname)

    suspend fun deleteUser(userId: Int) = loginRepository.deleteUser(userId)
}