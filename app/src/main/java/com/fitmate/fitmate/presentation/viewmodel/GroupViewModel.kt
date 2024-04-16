package com.fitmate.fitmate.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fitmate.fitmate.data.model.dto.EachFitResponse
import com.fitmate.fitmate.data.model.dto.FitGroup
import com.fitmate.fitmate.data.model.dto.FitGroupDetail
import com.fitmate.fitmate.data.model.dto.FitGroupProgress
import com.fitmate.fitmate.data.model.dto.GroupDetailResponse
import com.fitmate.fitmate.data.model.dto.GroupResponse
import com.fitmate.fitmate.domain.usecase.DBChatUseCase
import com.fitmate.fitmate.domain.usecase.GroupUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class GroupViewModel @Inject constructor(
    private val groupUseCase: GroupUseCase,
    private val dbChatUseCase: DBChatUseCase
) : ViewModel() {
    private val _fitGroups = MutableLiveData<GroupResponse>()
    val fitGroups: LiveData<GroupResponse> = _fitGroups

    private val _groupDetail = MutableLiveData<GroupDetailResponse>()
    val groupDetail: LiveData<GroupDetailResponse> = _groupDetail

    private val _fitGroupVotes = MutableLiveData<EachFitResponse>()
    val fitGroupVotes: LiveData<EachFitResponse> = _fitGroupVotes

    private val _fitGroup = MutableStateFlow<List<FitGroup>>(emptyList())
    val fitGroup: StateFlow<List<FitGroup>> = _fitGroup

    private val _fitMateList = MutableLiveData<FitGroupDetail>()
    val fitMateList: LiveData<FitGroupDetail> = _fitMateList

    private val _fitMateProgress = MutableLiveData<FitGroupProgress>()
    val fitMateProgress: LiveData<FitGroupProgress> = _fitMateProgress

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private var isDataLoadedOnce = false

    fun getFitGroups(withMaxGroup: Boolean, category: Int, pageNumber: Int, pageSize: Int) {
        viewModelScope.launch {
            val response = groupUseCase(withMaxGroup, category, pageNumber, pageSize)
            _fitGroups.value = response
        }
    }

    fun groupDetail(fitGroupId: Int) {
        viewModelScope.launch{
            val response = groupUseCase(fitGroupId)
            _groupDetail.value = response
        }
    }
    fun fetchFitGroupVotes() {
        viewModelScope.launch {
            val response = groupUseCase.eachFitGroupVotes()
            _fitGroupVotes.value = response.body()
        }
    }

    fun getFitMateList() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = groupUseCase.getFitMateList()
                if (response.isSuccessful) {
                    _fitMateList.value = response.body()
                }
                _isLoading.value = false
            } catch (e: Exception) {
                // 에러 처리
                _isLoading.value = false
            }
        }
    }

    fun getFitMateProgress() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = groupUseCase.getFitMateProgress()
                if(response.isSuccessful) {
                    _fitMateProgress.value = response.body()
                }
            } catch (e: Exception) {
                // 에러 처리
                _isLoading.value = false
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
}