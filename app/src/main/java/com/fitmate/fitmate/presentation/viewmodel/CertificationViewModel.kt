package com.fitmate.fitmate.presentation.viewmodel

import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fitmate.fitmate.domain.model.CertificationImage
import com.fitmate.fitmate.domain.model.DbCertification
import com.fitmate.fitmate.domain.usecase.DbCertificationUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.Instant
import javax.inject.Inject

enum class CertificateState { NON_PROCEEDING, ADDED_START_IMAGE, PROCEEDING }

@HiltViewModel
class CertificationViewModel @Inject constructor(
    private val dbCertificationUseCase: DbCertificationUseCase,
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

    //인증 화면을 진행중이던 상태로 설정하는 메서드
    fun setStateCertificateProceed() {
        _state.value = CertificateState.PROCEEDING
        //TODO room의 데이터를 가져와서 사진 데이터를 업데이트하는 적업 수행.
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


    //시작 이미지 첨부 메서드
    fun addEndImage(imageObject: CertificationImage) {
        endImageList2.add(imageObject)
        _endImageList.value = endImageList2
    }

    //종료 이미지 삭제 메서드
    fun removeEmdImage(index: Int) {
        endImageList2.removeAt(index)
        _endImageList.value = endImageList2
    }

    //데이터 베이스에서 데이터 조회
    val contentList = dbCertificationUseCase.loadList()
        .stateIn(
            initialValue = emptyList(),
            started = SharingStarted.WhileSubscribed(5000),
            scope = viewModelScope
        ).map { list ->
            list.filter { item ->
                item.id == 1
            }
        }

    fun getCertificationDataDb(id: Int) {
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                dbCertificationUseCase.getItemById(id).also { data->
                    Log.d("testt","db에서 불러온 사진 갯수:${data?.startImages?.size}")
                    withContext(Dispatchers.Main){
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
                    Log.d("testt","데이터 포장중")
                    add(data.imagesUri)
                }
            }
        )
        viewModelScope.launch {
            Log.d("testt",obj.startImages.size.toString())
            dbCertificationUseCase.save(obj).also {
                _doneEvent.value = Pair(true, if (it) "저장 완료" else "저장 실패")
            }
        }
    }

    fun deleteCertificationInfo() {
        viewModelScope.launch {
            dbCertificationUseCase.delete().also {
                _doneEvent.value = Pair(true, if (it) "삭제 완료" else "삭제 실패")
            }
        }
    }


}