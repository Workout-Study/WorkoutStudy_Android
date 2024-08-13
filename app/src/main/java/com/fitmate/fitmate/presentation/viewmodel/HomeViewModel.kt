package com.fitmate.fitmate.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.fitmate.fitmate.data.model.CertificationMapper.toCertificationTargetFitGroupResponse
import com.fitmate.fitmate.data.model.MyGroupNewsMapper.toMyGroupNews
import com.fitmate.fitmate.domain.model.FitGroupItem
import com.fitmate.fitmate.domain.model.MyGroupNews
import com.fitmate.fitmate.domain.usecase.MyGroupNewsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class HomeViewModel@Inject constructor(
    private val myGroupNewsUseCase: MyGroupNewsUseCase
): ViewModel() {
    private val _myFitGroupList = MutableLiveData<List<FitGroupItem>>()
    val myFitGroupList: LiveData<List<FitGroupItem>>
        get() = _myFitGroupList

    private val _pagingData = MutableStateFlow<PagingData<MyGroupNews>?>(null)
    val pagingData: StateFlow<PagingData<MyGroupNews>?> = _pagingData

    //내가 가입한 fit그룹 통신해서 가져오기
    fun getMyFitGroupList(userId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = myGroupNewsUseCase.getMyFitGroupList(userId)
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        val result = response.body()?.toCertificationTargetFitGroupResponse()
                        result?.let {
                            _myFitGroupList.value = it
                        }
                    }
                }
            } catch (e: Exception) {
                //통신 실패 했을 경우
            }
        }
    }

     fun getPagingGroupNews(
         userId: Int,
         pageNumber: Int,
         pageSize: Int,
     ) {
         viewModelScope.launch {
             try {
                 myGroupNewsUseCase.getMyGroupNews(userId, pageNumber, pageSize)
                     .cachedIn(this)
                     .collectLatest { list ->
                         // DTO 리스트를 도메인 모델 리스트로 변환
                        val myGroupNewsList = list.map {
                            it.toMyGroupNews()
                        }
                        _pagingData.value = myGroupNewsList
                    }
            } catch (e: Exception) {
                Log.d("tlqkf","그룹 소식 오류:$e")
            }
        }
    }

}