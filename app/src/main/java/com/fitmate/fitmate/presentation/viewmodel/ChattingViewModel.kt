package com.fitmate.fitmate.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.fitmate.fitmate.data.model.dto.ChatResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import androidx.lifecycle.viewModelScope
import com.fitmate.fitmate.data.model.dto.FitGroup
import com.fitmate.fitmate.data.model.dto.RetrieveFitGroup
import com.fitmate.fitmate.data.model.entity.ChatEntity
import com.fitmate.fitmate.domain.usecase.DBChatUseCase
import com.fitmate.fitmate.util.DateParseUtils
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ChattingViewModel @Inject constructor(private val dbChatUseCase: DBChatUseCase): ViewModel() {

    private val TAG = "ChattingViewModel"
    private val _fitGroup = MutableStateFlow<List<RetrieveFitGroup>>(emptyList())
    private val _chatResponse = MutableStateFlow<ChatResponse?>(null)
    private val _lastChatItem = MutableLiveData<ChatEntity?>()
    private val _isLoading = MutableStateFlow(false)
    private val _errorMessage = MutableLiveData<String?>()

    val fitGroup: StateFlow<List<RetrieveFitGroup>> = _fitGroup
    val chatResponse: StateFlow<ChatResponse?> = _chatResponse
    val lastChatItem: LiveData<ChatEntity?> = _lastChatItem
    val isLoading: StateFlow<Boolean> = _isLoading
    val errorMessage: LiveData<String?> = _errorMessage

    private var isDataLoadedOnce = false

    fun retrieveMessage() {
        viewModelScope.launch {
            val lastItem = dbChatUseCase.getLastChatItem()
            if (lastItem != null) {
                val messageId = lastItem.messageId
                val fitGroupId = lastItem.fitGroupId
                val fitMateId = lastItem.userId
                val messageTime = DateParseUtils.instantToString(lastItem.messageTime)
                val messageType = lastItem.messageType

                Log.d(TAG, "Requesting message with ID: $messageId, Group ID: $fitGroupId, Mate ID: $fitMateId, Time: $messageTime, Type: $messageType")
                _isLoading.value = true
                val response = dbChatUseCase.retrieveMessage(messageId, fitGroupId, fitMateId, messageTime, messageType)

                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        Log.d(TAG, "Response Success: ${response.body()}")
                        _chatResponse.value = response.body()
                    } else {
                        Log.d(TAG, "Response Failure: ${response.errorBody()?.string()}")
                    }
                    _isLoading.value = false
                }
            }else{
                _chatResponse.value = null
            }
        }
    }

    fun retrieveFitGroup(userId: String) {
        if (isDataLoadedOnce) return
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = dbChatUseCase.retrieveFitGroup(userId)

                withContext(Dispatchers.Main) {
                    val result = response.body()
                    _fitGroup.value = result!!
                    _isLoading.value = false
                    _errorMessage.value = null
                    isDataLoadedOnce = true
                }
            } catch (e: Exception) {
                Log.d(TAG, "Retrieve Group Error... $e")
                _errorMessage.value = "User Id가 존재하지 않거나, 그룹이 서버에 존재하지 않습니다. \n $e"
                _isLoading.value = false
            }

        }
    }

    fun clearErrorMessage() {
        _errorMessage.value = null
    }
}