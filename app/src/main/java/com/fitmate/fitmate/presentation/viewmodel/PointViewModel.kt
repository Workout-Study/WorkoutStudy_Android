package com.fitmate.fitmate.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.fitmate.fitmate.data.model.PointMapper.toPoint
import com.fitmate.fitmate.data.model.PointMapper.toPointHistoryContent
import com.fitmate.fitmate.domain.model.CategoryItem
import com.fitmate.fitmate.domain.model.Point
import com.fitmate.fitmate.domain.model.PointHistoryContent
import com.fitmate.fitmate.domain.usecase.PointUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onEmpty
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class PointViewModel @Inject constructor(
    private val pointUseCase: PointUseCase,
) : ViewModel() {
    private val _pointInfo = MutableLiveData<Point>()
    val pointInfo: LiveData<Point>
        get() = _pointInfo

    private val _pagingData = MutableStateFlow<PagingData<PointHistoryContent>?>(null)
    val pagingData: StateFlow<PagingData<PointHistoryContent>?> = _pagingData

    fun getPointInfo(pointOwnerId: Int, pointOwnerType: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val response = pointUseCase.getPointInfo(pointOwnerId, pointOwnerType)
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    _pointInfo.value = response.body()?.toPoint()
                }
            }

        }

    }

    fun getPagingPointHistory(
        pointOwnerId: Int,
        pointOwnerType: String,
        historyStartDate: String?,
        historyEndDate: String?,
        pageNumber: Int,
        pageSize: Int,
        tradeType: String?,
    ) {
        viewModelScope.launch {
            try {
                pointUseCase.getPointHistory(pointOwnerId, pointOwnerType, historyStartDate, historyEndDate, pageNumber, pageSize, tradeType)
                    .cachedIn(this)
                    .collectLatest { list ->
                        // DTO 리스트를 도메인 모델 리스트로 변환
                        val pointHistoryList = list.map {
                            it.toPointHistoryContent()
                        }
                        _pagingData.value = pointHistoryList
                    }
            } catch (e: Exception) {

            }
        }
    }
}