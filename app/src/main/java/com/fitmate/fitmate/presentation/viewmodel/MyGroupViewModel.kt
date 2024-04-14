package com.fitmate.fitmate.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fitmate.fitmate.data.model.dto.FitGroup
import com.fitmate.fitmate.domain.usecase.DBChatUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class MyGroupViewModel @Inject constructor(
    private val dbChatUseCase: DBChatUseCase
) : ViewModel() {

    private val _fitGroup = MutableStateFlow<List<FitGroup>>(emptyList())
    val fitGroup: StateFlow<List<FitGroup>> = _fitGroup

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private var isDataLoadedOnce = false

    fun retrieveFitGroup(fitMateId: Int) {
        if (isDataLoadedOnce) {
            return
        }

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
}