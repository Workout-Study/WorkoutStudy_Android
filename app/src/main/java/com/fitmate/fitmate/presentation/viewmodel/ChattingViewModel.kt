package com.fitmate.fitmate.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.fitmate.fitmate.data.model.dto.ChatResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import androidx.lifecycle.viewModelScope
import com.fitmate.fitmate.data.model.dto.EachFitResponse
import com.fitmate.fitmate.data.model.entity.ChatEntity
import com.fitmate.fitmate.domain.usecase.DBChatUseCase
import com.fitmate.fitmate.domain.usecase.GroupUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ChattingViewModel @Inject constructor(
    private val dbChatUseCase: DBChatUseCase
): ViewModel() {

    private val _chatResponse = MutableStateFlow<ChatResponse?>(null)
    val chatResponse: StateFlow<ChatResponse?> = _chatResponse

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _lastChatItem = MutableLiveData<ChatEntity?>()
    val lastChatItem: LiveData<ChatEntity?> = _lastChatItem

    fun retrieveMessage() {
        viewModelScope.launch {
            // 마지막으로 저장된 채팅 메시지 불러오기
            val lastItem = dbChatUseCase.getLastChatItem()

            // 로그로 마지막 채팅 메시지 정보 출력
            Log.d("ChattingViewModel", "Last Chat Item: $lastItem")

            // 마지막 채팅 메시지가 있으면, 그 정보를 사용하여 네트워크 요청 수행
            lastItem?.let {
                val messageId = it.messageId
                val fitGroupId = it.fitGroupId
                val fitMateId = it.fitMateId
                val messageTime = formatCustomDateTime(it.messageTime.toString())
                val messageType = it.messageType

                // 로그로 네트워크 요청 정보 출력
                Log.d("ChattingViewModel", "Requesting message with ID: $messageId, Group ID: $fitGroupId, Mate ID: $fitMateId, Time: $messageTime, Type: $messageType")

                // 여기에서 네트워크 요청 로직을 수행
                _isLoading.value = true
                val response = dbChatUseCase.retrieveMessage(messageId, fitGroupId, fitMateId, messageTime, messageType)

                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        // 로그로 응답 성공 메시지 출력
                        Log.d("ChattingViewModel", "Response Success: ${response.body()}")
                        _chatResponse.value = response.body()
                    } else {
                        // 로그로 응답 실패 메시지 출력
                        Log.d("ChattingViewModel", "Response Failure: ${response.errorBody()?.string()}")
                    }
                    _isLoading.value = false
                }
            }
        }
    }

    private fun formatCustomDateTime(isoDateTime: String): String {
        return isoDateTime.replace("T", " ")
    }
}