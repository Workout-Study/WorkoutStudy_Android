package com.fitmate.fitmate.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.fitmate.fitmate.domain.model.Bank
import com.fitmate.fitmate.domain.model.CertificationImage
import java.util.StringTokenizer

class MakeGroupViewModel: ViewModel() {
    val groupName = MutableLiveData<String>("")

    private val _bankInfo = MutableLiveData<Bank>(Bank(null, "은행 선택"))
    val bankInfo: LiveData<Bank>
        get() = _bankInfo

    val bankAccount = MutableLiveData<String>()

    private val _groupCategory = MutableLiveData<String>()
    val groupCategory: LiveData<String>
        get() = _groupCategory

    private val _groupFitCycle = MutableLiveData<Int>(0)
    val groupFitCycle:LiveData<Int>
        get() = _groupFitCycle

    private val _groupFitMateLimit = MutableLiveData<Int>(0)
    val groupFitMateLimit:LiveData<Int>
        get() = _groupFitMateLimit

    val groupContent = MutableLiveData<String>()


    private val groupImageList2 = mutableListOf<CertificationImage>()
    private val _groupImageList = MutableLiveData<List<CertificationImage>>()
    val groupImageList: LiveData<List<CertificationImage>>
        get() = _groupImageList

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
}