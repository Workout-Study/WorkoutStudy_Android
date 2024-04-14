package com.fitmate.fitmate.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fitmate.fitmate.data.model.dto.GroupDetailResponse
import com.fitmate.fitmate.data.model.dto.GroupResponse
import com.fitmate.fitmate.domain.usecase.GroupUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GroupViewModel @Inject constructor(private val groupUseCase: GroupUseCase) : ViewModel() {
    private val _fitGroups = MutableLiveData<GroupResponse>()
    val fitGroups: LiveData<GroupResponse> = _fitGroups

    private val _groupDetail = MutableLiveData<GroupDetailResponse>()
    val groupDetail: LiveData<GroupDetailResponse> = _groupDetail

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
}