package com.fitmate.fitmate.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fitmate.fitmate.data.model.VoteMapper.mapEachVoteCertificationResponseDto
import com.fitmate.fitmate.data.model.VoteMapper.toVoteRequestDto
import com.fitmate.fitmate.data.model.dto.FitGroupDetail
import com.fitmate.fitmate.data.model.dto.FitGroupProgress
import com.fitmate.fitmate.data.model.dto.GetFitGroupDetail
import com.fitmate.fitmate.data.model.dto.MyFitGroupVote
import com.fitmate.fitmate.data.model.dto.VoteResponseDto
import com.fitmate.fitmate.domain.model.EachVoteCertificationResponse
import com.fitmate.fitmate.domain.model.VoteRequest
import com.fitmate.fitmate.domain.usecase.GroupUseCase
import com.fitmate.fitmate.domain.usecase.VoteUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject


@HiltViewModel
class VoteViewModel @Inject constructor(
    private val voteUseCase: VoteUseCase,
    private val groupUseCase: GroupUseCase,

    ): ViewModel() {

    private val _groupDetail = MutableLiveData<GetFitGroupDetail>()
    val groupDetail: LiveData<GetFitGroupDetail> = _groupDetail

    private val _myGroupVotes = MediatorLiveData<Result<List<MyFitGroupVote>>>()
    val myGroupVotes: LiveData<Result<List<MyFitGroupVote>>> = _myGroupVotes

    private val _fitGroupVotes = MutableLiveData<EachVoteCertificationResponse>()
    val fitGroupVotes: LiveData<EachVoteCertificationResponse> = _fitGroupVotes

    private val _fitMateList = MutableLiveData<FitGroupDetail>()
    val fitMateList: LiveData<FitGroupDetail> = _fitMateList

    private val _fitMateProgress = MutableLiveData<FitGroupProgress>()
    val fitMateProgress: LiveData<FitGroupProgress> = _fitMateProgress

    private val _voteResponse = MutableLiveData<Response<VoteResponseDto>>()
    val voteResponseDto: LiveData<Response<VoteResponseDto>> = _voteResponse

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private var isDataLoadedOnce = false
    fun fetchMyFitGroupVotes(requestUserId: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val result = voteUseCase.myFitGroupVotes(requestUserId)
                _isLoading.value = false
            } catch (e: Exception) {
                _isLoading.value = false
            }
        }
    }

    fun getGroupDetail(fitGroupId:Int) {
        viewModelScope.launch {
            try {
                val response = groupUseCase.getFitGroupDetail(fitGroupId)
                _groupDetail.value = response
            } catch (e: Exception) {
                //통신 오류
            }
        }
    }

    fun fetchFitGroupVotes(fitGroupId: Int, userId: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = voteUseCase.eachFitGroupVotes(fitGroupId, userId)
                if(response.isSuccessful) {
                    response.body()?.let { dtoBody ->
                        Log.d("tlqkf","통신 결과: "+dtoBody.toString())
                        val mappingData = mapEachVoteCertificationResponseDto(dtoBody)
                        Log.d("tlqkf","매핑 결과: "+mappingData.toString())
                        _fitGroupVotes.value = mappingData
                    }
                }
            } catch (e: Exception) {
                _isLoading.value = false
            }
        }
    }

    fun getFitMateProgress(fitGroupId: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = voteUseCase.getFitMateProgress(fitGroupId)
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
            _isLoading.value = true
            try {
                val response = voteUseCase.registerVote(voteRequest.toVoteRequestDto())
                if(response.isSuccessful) {
                    _voteResponse.postValue(response)
                }
            } catch (e: Exception) {
                // 에러 처리
                _isLoading.value = false
            }
        }
    }
}