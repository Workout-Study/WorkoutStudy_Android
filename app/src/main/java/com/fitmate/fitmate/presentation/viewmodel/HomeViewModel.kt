package com.fitmate.fitmate.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.fitmate.fitmate.data.model.MyGroupNewsMapper.toMyGroupNews
import com.fitmate.fitmate.domain.model.MyGroupNews
import com.fitmate.fitmate.domain.usecase.MyGroupNewsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel@Inject constructor(
    private val myGroupNewsUseCase: MyGroupNewsUseCase
): ViewModel() {
    private val _pagingData = MutableStateFlow<PagingData<MyGroupNews>?>(null)
    val pagingData: StateFlow<PagingData<MyGroupNews>?> = _pagingData

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