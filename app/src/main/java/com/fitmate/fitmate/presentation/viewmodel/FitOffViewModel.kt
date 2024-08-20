package com.fitmate.fitmate.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fitmate.fitmate.data.model.FitOffMapper.mapFitOffListDtoToEntity
import com.fitmate.fitmate.data.model.FitOffMapper.toFitOffRequestDto
import com.fitmate.fitmate.data.model.FitOffMapper.toFitOffResponse
import com.fitmate.fitmate.domain.model.CertificationImage
import com.fitmate.fitmate.domain.model.FitOffList
import com.fitmate.fitmate.domain.model.FitOffRequest
import com.fitmate.fitmate.domain.model.FitOffResponse
import com.fitmate.fitmate.domain.model.ResponseRegisterFitGroup
import com.fitmate.fitmate.domain.usecase.FitOffUseCase
import com.fitmate.fitmate.domain.usecase.MyFitUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class FitOffViewModel @Inject constructor(
    private val fitOffUseCase: FitOffUseCase
):ViewModel() {
    //피트 오프 기간 라이브 데이터
    val fitOffDateRange = MutableLiveData<String?>(null)

    //피트 오프 사유 라이브 데이터
    val fitOffReason = MutableLiveData<String?>(null)

    private val  _registerFitOffResponse =  MutableLiveData<FitOffResponse>()
    val registerFitOffResponse: LiveData<FitOffResponse> get() = _registerFitOffResponse

    private val  _fitOffResponse =  MutableLiveData<FitOffList>()
    val fitOffResponse: LiveData<FitOffList> get() = _fitOffResponse

    fun registerFitOff(fitOffRequest: FitOffRequest) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = fitOffUseCase.registerFitOff(fitOffRequest.toFitOffRequestDto())
                if (response.isSuccessful){
                    withContext(Dispatchers.Main){
                        val result = response.body()?.toFitOffResponse()
                        result?.let {
                            _registerFitOffResponse.value = it
                        }
                    }
                }
            }catch (e:Exception){
            }
        }
    }

    fun getFitOffByUserId(userId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = fitOffUseCase.getFitOffByUserId(userId)
                if (response.isSuccessful){
                    withContext(Dispatchers.Main){
                        response.body()?.let {dto ->
                            val result = mapFitOffListDtoToEntity(dto)
                            _fitOffResponse.value = result
                        }
                    }
                }
            }catch (e:Exception){
            }
        }
    }


    fun getFitOffByGroupId(groupId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = fitOffUseCase.getFitOffByGroupId(groupId)
                if (response.isSuccessful){
                    withContext(Dispatchers.Main){
                        response.body()?.let {dto ->
                            val result = mapFitOffListDtoToEntity(dto)
                            _fitOffResponse.value = result
                        }
                    }
                }
            }catch (e:Exception){
            }
        }
    }

}