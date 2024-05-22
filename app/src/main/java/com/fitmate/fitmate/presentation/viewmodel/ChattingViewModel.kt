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
import com.fitmate.fitmate.data.model.entity.ChatEntity
import com.fitmate.fitmate.domain.usecase.DBChatUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ChattingViewModel @Inject constructor(private val dbChatUseCase: DBChatUseCase): ViewModel() {

    private val TAG = "ChattingViewModel"
    private val _fitGroup = MutableStateFlow<List<FitGroup>>(emptyList())
    private val _chatResponse = MutableStateFlow<ChatResponse?>(null)
    private val _lastChatItem = MutableLiveData<ChatEntity?>()
    private val _isLoading = MutableStateFlow(false)

    val fitGroup: StateFlow<List<FitGroup>> = _fitGroup
    val chatResponse: StateFlow<ChatResponse?> = _chatResponse
    val lastChatItem: LiveData<ChatEntity?> = _lastChatItem
    val isLoading: StateFlow<Boolean> = _isLoading

    private var isDataLoadedOnce = false

    fun retrieveMessage() {
        viewModelScope.launch {
            val lastItem = dbChatUseCase.getLastChatItem()
            Log.d("ChattingViewModel", "Last Chat Item: $lastItem")
            lastItem.let {
                val messageId = it.messageId
                val fitGroupId = it.fitGroupId
                val fitMateId = it.fitMateId
                val messageTime = formatCustomDateTime(it.messageTime.toString())
                val messageType = it.messageType

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
            }
        }
    }

    fun retrieveFitGroup(fitMateId: Int) {
        if (isDataLoadedOnce) return
        viewModelScope.launch {
            _isLoading.value = true
            val response = dbChatUseCase.retrieveFitGroup(fitMateId)

            withContext(Dispatchers.Main) {
                val result = response.body()
                _fitGroup.value = result!!
                _isLoading.value = false
                isDataLoadedOnce = true
            }
        }
    }

    private fun formatCustomDateTime(isoDateTime: String): String = isoDateTime.replace("T", " ")
}