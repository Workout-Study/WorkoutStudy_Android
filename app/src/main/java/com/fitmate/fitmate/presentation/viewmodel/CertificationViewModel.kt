package com.fitmate.fitmate.presentation.viewmodel

import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fitmate.fitmate.domain.model.CertificationImage
import com.fitmate.fitmate.domain.model.DbCertification
import com.fitmate.fitmate.domain.usecase.CertificationRecordNetworkUseCase
import com.fitmate.fitmate.domain.usecase.DbCertificationUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.Instant
import javax.inject.Inject

enum class CertificateState { NON_PROCEEDING, ADDED_START_IMAGE, PROCEEDING }

@HiltViewModel
class CertificationViewModel @Inject constructor(
    private val dbCertificationUseCase: DbCertificationUseCase,
    private val certificationRecordNetworkUseCase: CertificationRecordNetworkUseCase
) : ViewModel() {
    //인증 진행 상태
    private val _state = MutableLiveData<CertificateState>()
    val state: LiveData<CertificateState>
        get() = _state

    //시작 사진 데이터
    private val startImageList2 = mutableListOf<CertificationImage>()
    private val _startImageList = MutableLiveData<MutableList<CertificationImage>>()
    val startImageList: LiveData<MutableList<CertificationImage>>
        get() = _startImageList

    private val _certificationData = MutableLiveData<DbCertification?>()
    val certificationData: LiveData<DbCertification?>
        get() = _certificationData

    //종료 사진 데이터
    private val endImageList2 = mutableListOf<CertificationImage>()
    private val _endImageList = MutableLiveData<MutableList<CertificationImage>>()
    val endImageList: LiveData<MutableList<CertificationImage>>
        get() = _endImageList

    //db통신 처리 결과
    private val _doneEvent = MutableLiveData<Pair<Boolean, String>>()
    val doneEvent: LiveData<Pair<Boolean, String>>
        get() = _doneEvent


    private val _completeUpload = MutableLiveData<Boolean>()
    val completeUpload: LiveData<Boolean>
        get() = _completeUpload


    private val _urlMap = MutableLiveData<Map<String, MutableList<String>>>()
    val urlMap: LiveData<Map<String, MutableList<String>>>
        get() = _urlMap

    private val _networkPostState = MutableLiveData<Pair<Boolean, String>>()
    val networkPostState: LiveData<Pair<Boolean, String>>
        get() = _networkPostState

    //인증 화면을 진행중이던 상태로 설정하는 메서드
    fun setStateCertificateProceed() {
        _state.value = CertificateState.PROCEEDING
    }

    //인증 화면을 초기 상태로 설정하는 메서드
    fun setStateCertificateNonProceeding() {
        _state.value = CertificateState.NON_PROCEEDING

    }

    //초기 인증 화면에서 사진을 첨부했을 상태로 설정하는 메서드
    fun setStateCertificateAddedStartImage() {
        _state.value = CertificateState.ADDED_START_IMAGE
    }


    //시작 이미지 첨부 메서드
    fun addStartImage(imageObject: CertificationImage) {
        startImageList2.add(imageObject)
        _startImageList.value = startImageList2
    }

    //시작 이미지 삭제 메서드
    fun removeStartImage(index: Int) {
        startImageList2.removeAt(index)
        _startImageList.value = startImageList2
    }

    fun resetStartImage() {
        startImageList2.clear()
        _startImageList.value = startImageList2
    }


    //종료 이미지 첨부 메서드
    fun addEndImage(imageObject: CertificationImage) {
        endImageList2.add(imageObject)
        _endImageList.value = endImageList2
    }

    //종료 이미지 특정 사진 삭제 메서드
    fun removeEndImage(index: Int) {
        endImageList2.removeAt(index)
        _endImageList.value = endImageList2
    }

    //종료 이미지 라이브데이터 초기화
    fun resetEndImage() {
        endImageList2.clear()
        _endImageList.value = endImageList2
    }

    //room에서 인증 데이터 가져오기
    fun getCertificationDataDb(id: Int) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                dbCertificationUseCase.getItemById(id).also { data ->
                    withContext(Dispatchers.Main) {
                        _certificationData.value = data
                    }
                }
            }
        }
    }


    //Room에 시작 데이터 삽입(인증 시작을 눌렀을 경우)
    fun insertCertificateInitInfo() {
        val obj = DbCertification(
            id = 1,
            userId = "hyungoo",
            recordStartDate = Instant.now(),
            startImages = mutableListOf<Uri>().apply {
                _startImageList.value?.forEach { data ->
                    add(data.imagesUri)
                }
            }
        )
        viewModelScope.launch {
            dbCertificationUseCase.save(obj).also {
                _doneEvent.value = Pair(true, if (it) "저장 완료" else "저장 실패")
            }
        }
    }
    //room 데이터 삭제
    fun deleteCertificationInfo() {
        viewModelScope.launch {
            dbCertificationUseCase.delete().also {
                _doneEvent.value = Pair(true, if (it) "삭제 완료" else "삭제 실패")
            }
        }
    }

    //room 데이터 업데이트
    fun updateCertificationInfo(item: DbCertification) {
        viewModelScope.launch {
            dbCertificationUseCase.update(item).also {
                _doneEvent.value = Pair(true, if (it) "업데이트 완료" else "업데이트 실패")
            }
        }
    }

    //이미지 스토리지에 저장 및 url받기
    fun uploadImageAndGetUrl(item: DbCertification) {
        viewModelScope.launch {
            _urlMap.value =  dbCertificationUseCase.uploadAndGetImageUrl(item)
        }
    }

    //기록 백엔드에 전송
    fun postCertificationRecord(item:DbCertification) {
        viewModelScope.launch(Dispatchers.IO) {
            val response = certificationRecordNetworkUseCase.postCertificationRecord(item)
            withContext(Dispatchers.Main){
                if(response.isSuccessful) {
                    val result = response.body()
                    Log.d("networkLog","$result")
                    result?.let {
                        _networkPostState.value = Pair(true, if (it.isRegisterSuccess) "업로드 성공" else "업로드 실패")
                    }
                }
            }
        }
    }

    fun changeCheckUploadComplete() {
        _completeUpload.value = false
    }
}