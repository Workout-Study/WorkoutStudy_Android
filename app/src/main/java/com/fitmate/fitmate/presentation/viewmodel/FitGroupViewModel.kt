package com.fitmate.fitmate.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fitmate.fitmate.domain.FitGroupService.FitGroup
import com.fitmate.fitmate.domain.FitGroupService.FitGroupService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FitGroupViewModel @Inject constructor(
    private val fitGroupService: FitGroupService
) : ViewModel() {

    private val _fitGroups = MutableLiveData<List<FitGroup>>()
    val fitGroups: LiveData<List<FitGroup>> = _fitGroups

    fun fetchFitGroups(fitMateId: Int) {
        viewModelScope.launch {
            try {
                val response = fitGroupService.getFitGroup(fitMateId)
                _fitGroups.value = response
            } catch (e: Exception) {
                Log.e("Fetch Error", "Error fetching fit groups", e)
                // 에러 처리
            }
        }
    }
}
