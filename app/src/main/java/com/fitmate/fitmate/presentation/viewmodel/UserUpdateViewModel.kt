package com.fitmate.fitmate.presentation.viewmodel

import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fitmate.fitmate.data.model.dto.LoginSuccessResponse
import com.fitmate.fitmate.domain.usecase.LoginUseCase
import com.fitmate.fitmate.domain.usecase.PointUseCase
import com.google.firebase.Firebase
import com.google.firebase.storage.storage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserUpdateViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
): ViewModel() {
    private val _isImageUpload: MutableLiveData<String?> = MutableLiveData<String?>()
    val isImageUpload: LiveData<String?> get() = _isImageUpload

    private val _success = MutableLiveData<LoginSuccessResponse>()
    val success: LiveData<LoginSuccessResponse> = _success

    val updateUserImage = MutableLiveData<String?>(null)
    val updateUserNickName = MutableLiveData<String?>(null)


    fun imageUpload(userId: Int, uri: Uri) {
        val storage = Firebase.storage
        val storageRef = storage.getReference("user_profile")

        val fileName = "${userId}_profile.png"
        val userRef = storageRef.child(fileName)

        userRef.putFile(uri).addOnSuccessListener {
            userRef.downloadUrl.addOnSuccessListener { uri ->
                _isImageUpload.value = uri.toString()
                Log.d("tlqkf",isImageUpload.value.toString())
            }.addOnFailureListener {
                _isImageUpload.value = null
            }
        }.addOnFailureListener {
            _isImageUpload.value = null
        }
    }

    fun updateUserInfo(userToken: String, nickname: String, imageUrl: String?) {
        // TODO 이거 userID 말고 access토큰이어야함.
        viewModelScope.launch {
            try {
                val response = loginUseCase.updateNickname(userToken, nickname, imageUrl)
                _success.value = response
            } catch (e: Exception) {
                //오류 발생
            }
        }
    }
}