package com.fitmate.fitmate.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.fitmate.fitmate.data.model.dto.FitGroup
import com.fitmate.fitmate.data.model.dto.FitGroupFilter
import com.fitmate.fitmate.data.model.dto.GetFitGroupDetail
import com.fitmate.fitmate.data.model.dto.GetFitMateList
import com.fitmate.fitmate.data.model.dto.RegisterResponse
import com.fitmate.fitmate.domain.model.CategoryItem
import com.fitmate.fitmate.domain.usecase.GroupUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GroupViewModel @Inject constructor(
    private val groupUseCase: GroupUseCase,
) : ViewModel() {

    private val TAG = "GroupViewModel"

    private val _fitGroupFilter = MutableLiveData<FitGroupFilter>()
    val fitGroupFilter: LiveData<FitGroupFilter> = _fitGroupFilter

    private val _categoryItems = MutableLiveData<List<CategoryItem>>()
    val categoryItems: LiveData<List<CategoryItem>> = _categoryItems

    private val _groupDetail = MutableLiveData<GetFitGroupDetail>()
    val groupDetail: LiveData<GetFitGroupDetail> = _groupDetail

    private val _getMate = MutableLiveData<GetFitMateList>()
    val getMate: LiveData<GetFitMateList> = _getMate

    private val _fitGroup = MutableStateFlow<List<FitGroup>>(emptyList())
    val fitGroup: StateFlow<List<FitGroup>> = _fitGroup

    private val _register = MutableLiveData<RegisterResponse>()
    val register: LiveData<RegisterResponse> = _register

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> = _errorMessage

    private val _successMessage = MutableLiveData<String?>()
    val successMessage: LiveData<String?> = _successMessage

    private val _pagingData = MutableStateFlow<PagingData<CategoryItem>?>(null)
    val pagingData: StateFlow<PagingData<CategoryItem>?> = _pagingData


    fun getGroups(withMaxGroup: Boolean, category: Int, pageNumber: Int = 0, pageSize: Int = 8) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                groupUseCase.fitGroupFilter(withMaxGroup, category, pageNumber, pageSize)
                    .cachedIn(this).collectLatest { list ->
                    _pagingData.value = list.map {
                        CategoryItem(
                            title = it.fitGroupName,
                            fitCount = "${it.frequency}회 / 1주",
                            peopleCount = "${it.presentFitMateCount} / ${it.maxFitMate}",
                            comment = it.introduction,
                            fitGroupId = it.fitGroupId,
                            thumbnail = try {
                                it.multiMediaEndPoints[0]
                            } catch (e: Exception) {
                                "null"
                            }
                        )
                    }
                }
                _isLoading.value = false
            } catch (e: Exception) {
                Log.d(TAG, "There is NO DATA in Server. $e")
                _errorMessage.value = "서버에 데이터가 존재하지 않습니다. [ ${e} ]"
                _isLoading.value = false
            }
        }
    }

    fun getFitGroupDetail(fitGroupId: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = groupUseCase.getFitGroupDetail(fitGroupId)
                _groupDetail.value = response
                _isLoading.value = false
            } catch (e: Exception) {
                Log.d(TAG, "There is NO DATA in Server. $e")
                _errorMessage.value = "해당 그룹은 데이터가 존재하지 않습니다. [ ${e} ]"
                _isLoading.value = false
            }
        }
    }

    fun getFitMateList(fitGroupId: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = groupUseCase.getFitMateList(fitGroupId)
                _getMate.value = response.body()
                _isLoading.value = false
            } catch (e: Exception) {
                _isLoading.value = false
            }
        }
    }

    fun registerFitMate(requestUserId: Int, fitGroupId: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = groupUseCase.registerFitMate(requestUserId, fitGroupId)
                _register.value = response
                _errorMessage.value = null // 에러 메시지를 명시적으로 null로 설정
                _successMessage.value = "그룹 가입 완료" // 성공 메시지 설정
                _isLoading.value = false
            } catch (e: Exception) {
                Log.d(TAG, "Request user already included in fit group. $e")
                _errorMessage.value = "이미 해당 그룹에 가입되어 있습니다."
                _successMessage.value = null // 성공 메시지를 명시적으로 null로 설정
                _isLoading.value = false
            }
        }
    }

    fun clearErrorMessage() {
        _errorMessage.value = null
    }

    fun clearSuccessMessage() {
        _successMessage.value = null
    }
}