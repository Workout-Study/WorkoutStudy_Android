package com.fitmate.fitmate.presentation.viewmodel

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fitmate.fitmate.data.model.MakeFitGroupMapper.toRegisterFitGroupDto
import com.fitmate.fitmate.data.model.MakeFitGroupMapper.toResponseRegisterFitGroup
import com.fitmate.fitmate.domain.model.Bank
import com.fitmate.fitmate.domain.model.CertificationImage
import com.fitmate.fitmate.domain.model.RequestRegisterFitGroupBody
import com.fitmate.fitmate.domain.model.ResponseRegisterFitGroup
import com.fitmate.fitmate.domain.usecase.MakeFitGroupUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.StringTokenizer
import javax.inject.Inject

@HiltViewModel
class MakeGroupViewModel @Inject constructor(
    private val makeGroupUseCase: MakeFitGroupUseCase
): ViewModel() {
    //그룹 이름 라이브 데이터
    val groupName = MutableLiveData<String>("")

    //그룹 계좌 은행 정보 라이브 데이터
    private val _bankInfo = MutableLiveData<Bank>(Bank(null, "은행 선택"))
    val bankInfo: LiveData<Bank>
        get() = _bankInfo

    //그룹 계좌 번호 라이브 데이터
    val bankAccount = MutableLiveData<String>()

    //그룹 카테고리 라이브 데이터
    private val _groupCategory = MutableLiveData<String>()
    val groupCategory: LiveData<String>
        get() = _groupCategory

    //그룹 운동 주기 라이브 데이터
    private val _groupFitCycle = MutableLiveData<Int>(0)
    val groupFitCycle:LiveData<Int>
        get() = _groupFitCycle

    //그룹 최대 인원 수 라이브 데이터
    private val _groupFitMateLimit = MutableLiveData<Int>(0)
    val groupFitMateLimit:LiveData<Int>
        get() = _groupFitMateLimit

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
    private val _postResult = MutableLiveData<ResponseRegisterFitGroup>()
    val postResult: LiveData<ResponseRegisterFitGroup>
        get() = _postResult

    //은행 선택 메서드
    fun setBankInfo(data:Bank) {
        _bankInfo.value = data
    }

    //카테고리 설정 메서드
    fun setCategorySelect(data:String) {
        _groupCategory.value = data
    }

    //fit주기 설정 메서드
    fun setGroupFitCycle(data:Int){
        _groupFitCycle.value = data
    }

    //인원 제한 설정 메서드
    fun setGroupFitMateLimit(data:Int){
        _groupFitMateLimit.value = data
    }

    //사진 추가 메서드
    fun addImage(data: CertificationImage) {
        groupImageList2.add(data)
        _groupImageList.value = groupImageList2
    }

    //사진 삭제 메서드
    fun removeImage(index:Int) {
        groupImageList2.removeAt(index)
        _groupImageList.value = groupImageList2
    }

     fun uploadImageAndGetUrl(userId:String){
        viewModelScope.launch {
            val imageList = groupImageList.value?.map {
                it.imagesUri
            }
            _groupImageUrlList.value = makeGroupUseCase.uploadGroupImageAndGetUrl(userId, groupName.value ?: "알 수 없는 그룹", imageList ?: mutableListOf())
        }
    }

    fun postRegisterFitGroup(item: RequestRegisterFitGroupBody) {
        viewModelScope.launch {
            val response = makeGroupUseCase.postRegisterFitGroup(item.toRegisterFitGroupDto())
            if (response.isSuccessful){
                val result = response.body()
                result?.let { resultData ->
                    _postResult.value = resultData.toResponseRegisterFitGroup()
                }
            }
        }
    }
}