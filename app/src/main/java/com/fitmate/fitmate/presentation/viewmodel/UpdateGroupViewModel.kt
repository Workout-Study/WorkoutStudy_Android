package com.fitmate.fitmate.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fitmate.fitmate.R
import com.fitmate.fitmate.data.model.MakeFitGroupMapper.toRegisterFitGroupDto
import com.fitmate.fitmate.data.model.MakeFitGroupMapper.toResponseRegisterFitGroup
import com.fitmate.fitmate.data.model.MakeFitGroupMapper.toResponseUpdateFitGroup
import com.fitmate.fitmate.data.model.dto.GetFitGroupDetail
import com.fitmate.fitmate.domain.model.CertificationImage
import com.fitmate.fitmate.domain.model.RequestRegisterFitGroupBody
import com.fitmate.fitmate.domain.model.ResponseRegisterFitGroup
import com.fitmate.fitmate.domain.model.UpdateFitGroupResponse
import com.fitmate.fitmate.domain.usecase.MakeFitGroupUseCase
import com.fitmate.fitmate.util.GroupCategory
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UpdateGroupViewModel @Inject constructor(
    private val makeGroupUseCase: MakeFitGroupUseCase,
) : ViewModel() {
    //그룹 이름 라이브 데이터
    val groupName = MutableLiveData<String>("")

    //그룹 카테고리 라이브 데이터
    val groupCategory = MutableLiveData<String>("")

    //그룹 운동 주기 라이브 데이터
    val groupFitCycle = MutableLiveData<Int>(0)

    //그룹 최대 인원 수 라이브 데이터
    val groupFitMateLimit = MutableLiveData<Int>(0)

    //그룹 상세 정보 라이브 데이터
    val groupContent = MutableLiveData<String>()

    private val groupImageList2 = mutableListOf<CertificationImage>()

    //그룹 사진 Uri 라이브 데이터
    private val _groupImageList = MutableLiveData<List<CertificationImage>>()
    val groupImageList: LiveData<List<CertificationImage>>
        get() = _groupImageList

    //그룹 사진 Url 라이브 데이터
    private val _groupImageUrlList = MutableLiveData<List<String>>()
    val groupImageUrlList: LiveData<List<String>>
        get() = _groupImageUrlList

    //최종 post 결과를 받는 라이브 데이터
    private val _postResult = MutableLiveData<UpdateFitGroupResponse>()
    val postResult: LiveData<UpdateFitGroupResponse>
        get() = _postResult

    //카테고리 설정 메서드
    fun setCategorySelect(data: String) {
        groupCategory.value = data
    }

    //fit주기 설정 메서드
    fun setGroupFitCycle(data: Int) {
        groupFitCycle.value = data
    }

    //인원 제한 설정 메서드
    fun setGroupFitMateLimit(data: Int) {
        groupFitMateLimit.value = data
    }

    //사진 추가 메서드
    fun addImage(data: CertificationImage) {
        groupImageList2.add(data)
        _groupImageList.value = groupImageList2
    }

    //사진 삭제 메서드
    fun removeImage(index: Int) {
        groupImageList2.removeAt(index)
        _groupImageList.value = groupImageList2
    }

    fun uploadImageAndGetUrl(userId: String) {
        viewModelScope.launch {
            val imageList = groupImageList.value?.map {
                it.imagesUri
            }
            _groupImageUrlList.value = makeGroupUseCase.uploadGroupImageAndGetUrl(
                userId,
                groupName.value ?: "알 수 없는 그룹",
                imageList ?: mutableListOf()
            )
        }
    }

    fun bindData(item: GetFitGroupDetail) {
        groupName.value = item.fitGroupName
        groupCategory.value = when(item.category) {
            1 ->  "등산" 2 ->  "생활 체육" 3 ->  "웨이트" 4 ->  "수영" 5 ->  "축구" 6 ->  "농구" 7 ->  "야구" 8 -> "바이킹" 9 -> "클라이밍"
            else -> null
        }
        groupFitCycle.value = item.frequency
        groupFitMateLimit.value = item.maxFitMate
        groupContent.value = item.introduction
    }


    fun postUpdateFitGroup(groupid:Int, item: RequestRegisterFitGroupBody) {
        viewModelScope.launch {
            val response = makeGroupUseCase.updateFitGroup(groupid, item.toRegisterFitGroupDto())
            if (response.isSuccessful) {
                val result = response.body()
                result?.let { resultData ->
                    _postResult.value = resultData.toResponseUpdateFitGroup()
                }
            }
        }
    }
}