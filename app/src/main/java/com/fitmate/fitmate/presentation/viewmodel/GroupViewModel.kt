package com.fitmate.fitmate.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fitmate.fitmate.data.model.dto.EachFitResponse
import com.fitmate.fitmate.data.model.dto.FitGroupDetail
import com.fitmate.fitmate.data.model.dto.FitGroupProgress
import com.fitmate.fitmate.data.model.dto.GroupDetailResponse
import com.fitmate.fitmate.data.model.dto.GroupResponse
import com.fitmate.fitmate.data.model.dto.VoteRequest
import com.fitmate.fitmate.data.model.dto.VoteResponse
import com.fitmate.fitmate.domain.usecase.DBChatUseCase
import com.fitmate.fitmate.domain.usecase.GroupUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class GroupViewModel @Inject constructor(
    private val groupUseCase: GroupUseCase
) : ViewModel() {
    private val _fitGroups = MutableLiveData<GroupResponse>()
    val fitGroups: LiveData<GroupResponse> = _fitGroups

    private val _groupDetail = MutableLiveData<GroupDetailResponse>()
    val groupDetail: LiveData<GroupDetailResponse> = _groupDetail

    private val _fitGroupVotes = MutableLiveData<EachFitResponse>()
    val fitGroupVotes: LiveData<EachFitResponse> = _fitGroupVotes

    private val _fitMateList = MutableLiveData<FitGroupDetail>()
    val fitMateList: LiveData<FitGroupDetail> = _fitMateList

    private val _fitMateProgress = MutableLiveData<FitGroupProgress>()
    val fitMateProgress: LiveData<FitGroupProgress> = _fitMateProgress

    private val _voteResponse = MutableLiveData<Response<VoteResponse>>()
    val voteResponse: LiveData<Response<VoteResponse>> = _voteResponse


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

    fun registerVote(voteRequest: VoteRequest) {
        viewModelScope.launch {
            val response = groupUseCase.registerVote(voteRequest)
            _voteResponse.postValue(response)
        }
    }

}