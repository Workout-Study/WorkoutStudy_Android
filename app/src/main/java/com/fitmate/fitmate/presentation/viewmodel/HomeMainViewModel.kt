package com.fitmate.fitmate.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fitmate.fitmate.data.model.dto.MyFitGroupVote
import com.fitmate.fitmate.domain.usecase.GroupUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeMainViewModel @Inject constructor(
    private val groupUseCase: GroupUseCase
): ViewModel() {
    private val _fitGroupVotes = MediatorLiveData<Result<List<MyFitGroupVote>>>()
    val fitGroupVotes: LiveData<Result<List<MyFitGroupVote>>> = _fitGroupVotes

    fun fetchMyFitGroupVotes() {
        viewModelScope.launch {
            val result = groupUseCase.myFitGroupVotes()
            _fitGroupVotes.value = result
        }
    }
}
