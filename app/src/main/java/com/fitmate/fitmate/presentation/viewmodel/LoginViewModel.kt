package com.fitmate.fitmate.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fitmate.fitmate.data.model.dto.LoginResponse
import com.fitmate.fitmate.data.model.dto.LoginSuccessResponse
import com.fitmate.fitmate.data.model.dto.UserResponse
import com.fitmate.fitmate.domain.usecase.LoginUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase
) : ViewModel() {

    private val TAG = "LoginViewModel"

    private val _success = MutableLiveData<LoginSuccessResponse>()
    val success: LiveData<LoginSuccessResponse> = _success

    private val _user = MutableLiveData<LoginResponse?>()
    val user: LiveData<LoginResponse?> = _user

    private val _nickname = MutableLiveData<UserResponse>()
    val nickname: LiveData<UserResponse> = _nickname

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> = _errorMessage

    private val _logoutComplete = MutableLiveData<Boolean>()
    val logoutComplete: LiveData<Boolean> = _logoutComplete

    var platform: String? = null

    fun tokenValid(accessToken: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = loginUseCase.tokenValid(accessToken)
                _success.value = response
                _isLoading.value = false
            } catch (e: Exception) {
                Log.d(TAG, "token valid Error... $e")
                _isLoading.value = false
            }
        }
    }

    fun login(code: String, token: String, imageUrl: String, platform: String) {
        this.platform = platform
        viewModelScope.launch {
            _isLoading.value = true
            try {
                _user.value = when(platform) {
                    "naver" -> loginUseCase.loginNaver(code, token, imageUrl)
                    "kakao" -> loginUseCase.loginKakao(code, token, imageUrl)
                    else -> null
                }
                _isLoading.value = false
            } catch (e: Exception) {
                Log.d(TAG, "login Error... $e")
                _errorMessage.value = "로그인을 할 수 없습니다. 다시 시도해주세요."
                _isLoading.value = false
            }
        }
    }

    fun logout(accessToken: String, platform: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = when(platform) {
                    "naver" -> loginUseCase.logoutNaver(accessToken)
                    "kakao" -> loginUseCase.logoutKakao(accessToken)
                    else -> throw IllegalArgumentException("Unknown platform: $platform")
                }
                _success.value = response
                _logoutComplete.value = true // 로그아웃 완료 상태 업데이트
            } catch (e: Exception) {
                Log.d(TAG, "logout Error: ${e.message}")
                _errorMessage.value = "로그아웃을 할 수 없습니다. 다시 시도해주세요."
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun getUserInfo(userId: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = loginUseCase.getUserInfo(userId)
                _nickname.value = response
                _isLoading.value = false
            } catch (e: Exception) {
                Log.d(TAG, "getUserInfo Error... $e")
                _isLoading.value = false
            }
        }
    }

    fun updateNickname(userId: String, nickname: String, imageUrl: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = loginUseCase.updateNickname(userId, nickname, imageUrl)
                _success.value = response
                _isLoading.value = false
            } catch (e: Exception) {
                Log.d(TAG, "update nickname Error... $e")
                _isLoading.value = false
            }
        }
    }

    fun deleteUser(userId: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = loginUseCase.deleteUser(userId)
                _success.value = response
                _isLoading.value = false
            } catch (e: Exception) {
                Log.d(TAG, "delete User Error... $e")
                _isLoading.value = false
            }
        }
    }
}
