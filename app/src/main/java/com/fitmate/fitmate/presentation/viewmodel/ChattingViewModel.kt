package com.fitmate.fitmate.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.fitmate.fitmate.data.model.dto.ChatResponse
import com.fitmate.fitmate.domain.usecase.NetworkUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import androidx.lifecycle.viewModelScope
import com.fitmate.fitmate.data.model.entity.ChatEntity
import com.fitmate.fitmate.domain.usecase.DBChatUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class ChattingViewModel @Inject constructor(
    private val networkUseCase: NetworkUseCase, private val dbChatUseCase: DBChatUseCase
): ViewModel() {

    private val _chatResponse = MutableStateFlow<ChatResponse?>(null)
    val chatResponse: StateFlow<ChatResponse?> = _chatResponse

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _lastChatItem = MutableLiveData<ChatEntity?>()
    val lastChatItem: LiveData<ChatEntity?> = _lastChatItem

    fun retrieveMessage(messageId: String, fitGroupId: Int, fitMateId: Int, messageTime: String, messageType: String) {
        viewModelScope.launch {
            _isLoading.value = true
            val response = networkUseCase.retrieveMessage(messageId, fitGroupId, fitMateId, messageTime, messageType)

            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    _chatResponse.value = response.body()
                }
                _isLoading.value = false
            }
        }
    }
// TODO 지금은 임시지만 나중에 채팅이 다른 디바이스에서도 서로 통신이 가능할 경우, 해당 함수를 사용해야 함.
// TODO 현재는 기기 1개 fitmateId 1개로 해서 이렇게 될 수 밖에 없지만, 추후에 복수 이상의 fitMateId가 들어오고 서로 채팅을 치고 안보는 상황에 해당 함수를 사용해야 함.

//    fun retrieveMessage() {
//        viewModelScope.launch {
//            // 마지막으로 저장된 채팅 메시지 불러오기
//            val lastItem = dbChatUseCase.getLastChatItem()
//
//            // 마지막 채팅 메시지가 있으면, 그 정보를 사용하여 네트워크 요청 수행
//            lastItem?.let {
//                val messageId = it.messageId
//                val fitGroupId = it.fitGroupId
//                val fitMateId = it.fitMateId
//                val messageTime = formatMessageTime(it.messageTime.toString())
//                val messageType = it.messageType
//
//                // 여기에서 네트워크 요청 로직을 수행
//                _isLoading.value = true
//                val response = networkUseCase.retrieveMessage(messageId, fitGroupId, fitMateId, messageTime, messageType)
//
//                withContext(Dispatchers.Main) {
//                    if (response.isSuccessful) {
//                        _chatResponse.value = response.body()
//                    }
//                    _isLoading.value = false
//                }
//            }
//        }
//    }
    private fun formatMessageTime(isoDateTime: String): String {
        // ISO 8601 형식의 문자열을 LocalDateTime으로 파싱
        val dateTime = LocalDateTime.parse(isoDateTime)

        // 원하는 형식으로 변환
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSS")
        return dateTime.format(formatter)
    }

}