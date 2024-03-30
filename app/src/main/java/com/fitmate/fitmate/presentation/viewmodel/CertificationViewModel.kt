package com.fitmate.fitmate.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.fitmate.fitmate.domain.model.CertificationImage
import com.fitmate.fitmate.domain.usecase.DbCertificationUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

enum class CertificateState{NON_PROCEEDING, ADDED_START_IMAGE, PROCEEDING}
@HiltViewModel
class CertificationViewModel @Inject constructor(
    private val dbCertificationUseCase: DbCertificationUseCase,
): ViewModel() {
    //인증 진행 상태
    private val _state = MutableLiveData<CertificateState>()
    val state: LiveData<CertificateState>
        get() = _state

    //시작 사진 데이터
    private val startImageList2 = mutableListOf<CertificationImage>()
    private val _startImageList = MutableLiveData<MutableList<CertificationImage>>()
    val startImageList: LiveData<MutableList<CertificationImage>>
        get() = _startImageList

    //인증 화면을 진행중이던 상태로 설정하는 메서드
    fun setStateCertificateProceed(){
        _state.value = CertificateState.PROCEEDING
        //TODO room의 데이터를 가져와서 사진 데이터를 업데이트하는 적업 수행.
    }

    //인증 화면을 초기 상태로 설정하는 메서드
    fun setStateCertificateNonProceeding(){
        _state.value = CertificateState.NON_PROCEEDING

    }

    //초기 인증 화면에서 사진을 첨부했을 상태로 설정하는 메서드
    fun setStateCertificateAddedStartImage(){
        _state.value = CertificateState.ADDED_START_IMAGE
    }


    //시작 이미지 첨부 메서드
    fun addStartImage(imageObject: CertificationImage) {
        startImageList2.add(imageObject)
        _startImageList.value = startImageList2
    }

    //시작 이미지 삭제 메서드
    fun removeStartImage(index:Int){
        startImageList2.removeAt(index)
        _startImageList.value = startImageList2
    }

}