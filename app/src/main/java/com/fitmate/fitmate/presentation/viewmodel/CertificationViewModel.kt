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
import com.google.firebase.Firebase
import com.google.firebase.storage.storage
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

    //파이어베이스에 저장하고 나온 url데이터들
    private val startImages = mutableListOf<String>()
    private val _startImageUrl = MutableLiveData<List<String>>()
    val startImageUrl: LiveData<List<String>>
        get() = _startImageUrl

    private val endImages = mutableListOf<String>()
    private val _endImageUrl = MutableLiveData<List<String>>()
    val endImageUrl: LiveData<List<String>>
        get() = _endImageUrl

    private val _completeUpload = MutableLiveData<Boolean>()
    val completeUpload: LiveData<Boolean>
        get() = _completeUpload

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

    fun resetStartImage() {
        startImageList2.clear()
        _startImageList.value = startImageList2
    }


    //종료 이미지 첨부 메서드
    fun addEndImage(imageObject: CertificationImage) {
        endImageList2.add(imageObject)
        _endImageList.value = endImageList2
    }

    //종료 이미지 삭제 메서드
    fun removeEndImage(index: Int) {
        endImageList2.removeAt(index)
        _endImageList.value = endImageList2
    }

    fun resetEndImage() {
        endImageList2.clear()
        _endImageList.value = endImageList2
    }


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

    fun deleteCertificationInfo() {
        viewModelScope.launch {
            dbCertificationUseCase.delete().also {
                _doneEvent.value = Pair(true, if (it) "삭제 완료" else "삭제 실패")
            }
        }
    }

    fun updateCertificationInfo(item: DbCertification) {
        viewModelScope.launch {
            dbCertificationUseCase.update(item).also {
                _doneEvent.value = Pair(true, if (it) "업데이트 완료" else "업데이트 실패")
            }
        }
    }


    fun imageUpload(item: DbCertification) {
        val storage = Firebase.storage
        val storageRef = storage.getReference("user_certificate")
        var startfileName: String = ""
        var endfileName: String = ""

        // 이미지 업로드 완료 카운트
        var startImageUploadCount = 0
        var endImageUploadCount = 0

        // 시작 이미지 업로드
        repeat(item.startImages.size) { index ->
            startfileName = "${item.userId}_certificate_${item.recordStartDate}_start_${index}"
            val userRef = storageRef.child(startfileName)

            userRef.putFile(item.startImages[index]).addOnSuccessListener { taskSnapshot ->
                userRef.downloadUrl.addOnSuccessListener { downloadUri ->
                    startImages.add(downloadUri.toString())
                    _startImageUrl.value = startImages

                    // 시작 이미지 업로드 완료 카운트 증가
                    startImageUploadCount++

                    // 모든 이미지 업로드 완료 여부 확인
                    checkUploadComplete(startImageUploadCount, item.startImages.size, endImageUploadCount, item.endImages?.size ?: 0)
                }.addOnFailureListener {
                    // 실패 처리
                }
            }.addOnFailureListener {
                Log.d("log_storage", "Upload Failure")
            }
        }

        // 끝 이미지 업로드
        repeat(item.endImages?.size ?: 0) { index ->
            endfileName = "${item.userId}_certificate_${item.recordEndDate}_end_${index}"
            val userRef = storageRef.child(endfileName)

            userRef.putFile(item.endImages!![index]).addOnSuccessListener { taskSnapshot ->
                userRef.downloadUrl.addOnSuccessListener { downloadUri ->
                    endImages.add(downloadUri.toString())
                    _endImageUrl.value = endImages
                    Log.d("rkdgusrn","끝 사진 저장중")

                    // 끝 이미지 업로드 완료 카운트 증가
                    endImageUploadCount++

                    // 모든 이미지 업로드 완료 여부 확인
                    checkUploadComplete(startImageUploadCount, item.startImages.size, endImageUploadCount, item.endImages.size ?: 0)
                }.addOnFailureListener {
                    // 실패 처리
                }
            }.addOnFailureListener {
                Log.d("log_storage", "Upload Failure")
            }
        }
    }

    // 모든 이미지 업로드 완료 여부 확인
    fun checkUploadComplete(startCount: Int, totalStartCount: Int, endCount: Int, totalEndCount: Int) {
        if (startCount == totalStartCount && endCount == totalEndCount) {
            // 모든 이미지 업로드 완료
            _completeUpload.value = true
        }
    }
    fun changeCheckUploadComplete() {
        _completeUpload.value = false
    }

}