package com.fitmate.fitmate.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fitmate.fitmate.data.model.MyFitMapper.toMyFitProgressResponse
import com.fitmate.fitmate.domain.model.FitProgressItem
import com.fitmate.fitmate.domain.usecase.MyFitUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class MyFitViewModel @Inject constructor(
    private val myFitUseCase: MyFitUseCase
): ViewModel() {
    private val _fitProgressItem = MutableLiveData<List<FitProgressItem>>()
    val fitProgressItem: LiveData<List<FitProgressItem>>
        get() = _fitProgressItem

    //내 fit 진척도 가져오는 메서드
    fun getMyFitProgress(userId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val response = myFitUseCase.getMyFitProgress(userId)
            if (response.isSuccessful){
                withContext(Dispatchers.Main){
                    val result = response.body()?.toMyFitProgressResponse()
                    result?.let {
                        _fitProgressItem.value = it
                    }
                }
            }
        }
    }

}