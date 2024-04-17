package com.fitmate.fitmate.presentation.ui.certificate

import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.core.content.PermissionChecker
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.fitmate.fitmate.MainActivity
import com.fitmate.fitmate.R
import com.fitmate.fitmate.databinding.FragmentCertificateBinding
import com.fitmate.fitmate.domain.model.CertificationImage
import com.fitmate.fitmate.domain.model.FitGroupItem
import com.fitmate.fitmate.domain.model.ResisterCertificationRecord
import com.fitmate.fitmate.presentation.ui.certificate.list.adapter.CertificationImageAdapter
import com.fitmate.fitmate.presentation.viewmodel.CertificateState
import com.fitmate.fitmate.presentation.viewmodel.CertificationViewModel
import com.fitmate.fitmate.util.ControlActivityInterface
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import java.time.Instant

@AndroidEntryPoint
class CertificateFragment : Fragment() {
    companion object {
        //사진 최대 선택 가능 갯수
        private const val IMAGE_PICK_MAX = 5

        enum class NetworkState {
            STATE_POST_BACK_END, STATE_UPLOAD_STORAGE, STATE_NON, STATE_TARGET_GROUP
        }
    }

    private lateinit var binding: FragmentCertificateBinding
    private lateinit var controlActivityInterface: ControlActivityInterface
    private lateinit var certificationStartImageAdapter: CertificationImageAdapter
    private lateinit var certificationEndImageAdapter: CertificationImageAdapter
    private val viewModel: CertificationViewModel by viewModels()
    private var pickMultipleMedia = activityResultLauncher()
    private var totalElapsedTime: Long = 0L
    private var networkState: NetworkState = NetworkState.STATE_NON

    //서비스의 스톱워치 시간대를 가져오는 브로드캐스트 리시버
    private var broadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val elapsedTime = intent?.getLongExtra("elapsedTime", 0) ?: 0
            totalElapsedTime = elapsedTime
            binding.textViewCertificateTimer.text = formatTime(elapsedTime)
        }
    }

    //서비스의 스톱워치 종료 여부를 가져오는 브로드캐스트 리시버(진행중인 상태에서만 구독함.)
    private var broadcastReceiver2 = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val endToken = intent?.getBooleanExtra("bye", false) ?: false
            closeFragment(endToken)
        }
    }

    //알림 권한
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private val permissionResult = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            viewModel.insertCertificateInitInfo()
        } else {
            if (!shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS)) {
                showNotificationPermissionDialog()
            } else {
                viewModel.insertCertificateInitInfo()
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        //초기 설정
        binding = FragmentCertificateBinding.inflate(layoutInflater)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel
        binding.materialToolbarCertificate.setupWithNavController(findNavController())
        //바텀 네비 삭제.
        removeBottomNavi()

        //리사이클러뷰 어뎁터 초기화
        initRecyclerviewAdapter()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.setStateCertificateNonProceeding()
        viewModel.getCertificationDataDb(1)
        viewModel.certificationData.observe(viewLifecycleOwner) {
            if (it != null) {
                if (it.recordEndDate == null && networkState == NetworkState.STATE_NON) {
                    viewModel.setStateCertificateProceed()
                } else if (networkState == NetworkState.STATE_POST_BACK_END) {
                    viewModel.postCertificationRecord(it)
                } else if (networkState == NetworkState.STATE_UPLOAD_STORAGE) {
                    viewModel.uploadImageAndGetUrl(it)
                }
            } else {
                viewModel.setStateCertificateNonProceeding()
            }
        }
        //room과의 통신 결과를 구독
        viewModel.doneEvent.observe(viewLifecycleOwner) {
            when (it.second) {
                "저장 완료" -> {
                    //room에 저장이 되었을 때 타이머 실행(포그라운드 서비스)
                    val intent = Intent(
                        this@CertificateFragment.context,
                        StopWatchService::class.java
                    ).apply {
                        action = STOP_WATCH_START
                    }
                    requireContext().startService(intent)
                    //room에 초기 데이터 저장
                    viewModel.getCertificationDataDb(1)
                }

                "업데이트 완료" -> {
                    viewModel.getCertificationDataDb(1)
                }

                "삭제 완료" -> {
                    if (networkState == NetworkState.STATE_TARGET_GROUP) {
                        //TODO 인증 완전 완료 상태.
                        findNavController().popBackStack()
                    } else {
                        //인증 취소되었을 때 또는 업로드에 실패했을 경우.
                        val intent = Intent(
                            this@CertificateFragment.context,
                            StopWatchService::class.java
                        ).apply {
                            action = STOP_WATCH_RESET
                        }
                        requireContext().startService(intent)
                        LocalBroadcastManager.getInstance(requireContext())
                            .unregisterReceiver(broadcastReceiver2)
                        setRecyclerViewState(true)
                        viewModel.resetStartImage()
                        viewModel.resetEndImage()
                        viewModel.resetCertificationLiveData()
                        binding.textViewCertificateTimer.text =
                            getString(R.string.certificate_scr_timer)
                        networkState = NetworkState.STATE_NON
                        viewModel.setStateCertificateNonProceeding()
                    }
                }
            }
        }

        viewModel.urlMap.observe(viewLifecycleOwner) { urlMap ->
            Log.d("testt",viewModel.selectedTarget.toString())
            //TODO 해당 Url을 room에 저장하고 가져와서 백엔드에 전달한다.
            val startUrl = urlMap["startUrls"]
            val endUrl = urlMap["endUrls"]

            val obj = viewModel.certificationData.value?.copy(
                startImagesUrl = startUrl,
                endImagesUrl = endUrl
            )
            obj?.let {
                networkState = NetworkState.STATE_POST_BACK_END
                viewModel.updateCertificationInfo(it)
            }

        }

        //포스트 통신 수행 결과
        viewModel.networkPostState.observe(viewLifecycleOwner) {
            if (it.isRegisterSuccess) {
                networkState = NetworkState.STATE_TARGET_GROUP
                //TODO 타겟 그룹으로 최종 통신 수행 해야함(아래 코드를 해당 통신 옵저버로 이동시켜야함.)
                it.fitRecordId?.let { recordId ->
                    val resisterObj = ResisterCertificationRecord("hyungoo",recordId,viewModel.selectedTarget)
                    viewModel.postResisterCertificationRecord(resisterObj)
                }

            } else {
                loadingTaskSettingEnd()
                certificationReset()
                Toast.makeText(requireContext(), "업로드에 실패했습니다", Toast.LENGTH_SHORT).show()
            }
        }

        //최종 통신 수행 결과
        viewModel.networkPostState2.observe(viewLifecycleOwner){
            if (it.isRegisterSuccess){
                loadingTaskSettingEnd()
                certificationReset()
            }
        }

        viewModel.myFitGroupData.observe(viewLifecycleOwner){
            loadingTaskSettingEnd()
            showSelectCertificateGroup(it)
        }

        //기록 시작 이미지 첨부 및 삭제 여부를 구독
        viewModel.startImageList.observe(viewLifecycleOwner) {
            certificationStartImageAdapter.submitList(it)
            certificationStartImageAdapter.notifyDataSetChanged()
            if (it.isEmpty()) {
                viewModel.setStateCertificateNonProceeding()
            } else {
                viewModel.setStateCertificateAddedStartImage()
            }
        }

        // 기록 종료 이미지 첨부 및 삭제 여부 구독
        viewModel.endImageList.observe(viewLifecycleOwner) {
            certificationEndImageAdapter.submitList(it)
            certificationEndImageAdapter.notifyDataSetChanged()
        }

        //화면 상태에 따른 설정을 위한 state 구독
        viewModel.state.observe(viewLifecycleOwner) { state ->
            when (state) {
                //인증 진행중인 상태
                CertificateState.PROCEEDING -> {
                    //사진 추가 클릭 리스너 정의 및 설정
                    setEndImageAddButtonClick()
                    //리셋버튼 정의 및 설정
                    binding.buttonCertificateReset.setOnClickListener {
                        certificationReset()
                    }
                    //시작 사진 리사이클러뷰 삭제 막기
                    setRecyclerViewState(false)

                    //리사이클러뷰 업데이트
                    certificationStartImageAdapter.submitList(viewModel.certificationData.value!!.startImages.map {
                        CertificationImage(
                            it
                        )
                    })

                    //인증 종료 버튼 정의 및 설정
                    binding.buttonCertificateConfirm.setOnClickListener {
                        //마지막 사진 list.size가 0보다 크지않다면
                        if ((viewModel.endImageList.value?.size ?: 0) <= 0) {
                            Toast.makeText(
                                requireContext(),
                                getString(R.string.certificate_scr_end_image_check_warning),
                                Toast.LENGTH_SHORT
                            ).show()
                            return@setOnClickListener
                        } else {

                            //그룹 선택하러 이동
                            loadingTaskSettingStart()
                            viewModel.getMyFitGroup("hyungoo")
                        }

                    }

                    // 서비스 종료에 관련된 브로드 캐스트 구독
                    val filter = IntentFilter("end_service")
                    LocalBroadcastManager.getInstance(requireContext())
                        .registerReceiver(broadcastReceiver2, filter)
                }

                //사진 첨부가 되었고 인증 진행이 가능한 상태
                CertificateState.ADDED_START_IMAGE -> {
                    binding.buttonCertificateConfirm.setOnClickListener {
                        //33버전 이상일 때 권한 요청
                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
                            permissionResult.launch(
                                Manifest.permission.POST_NOTIFICATIONS
                            )
                        } else {
                            viewModel.insertCertificateInitInfo()
                        }
                    }
                }
                //최초 상태(아무것도 안한 상태)
                CertificateState.NON_PROCEEDING -> {
                    //스타트 사진 이미지 첨부 버튼(ImageView) 설정
                    setStartImageAddButtonClick()
                }

                else -> {}
            }
        }
    }

    override fun onResume() {
        super.onResume()
        val filter = IntentFilter("timer-update")
        //타이머 브로드 캐스트 구독
        LocalBroadcastManager.getInstance(requireContext())
            .registerReceiver(broadcastReceiver, filter)
    }

    override fun onPause() {
        super.onPause()
        //브로드 캐스트 리시버 구독 해제
        LocalBroadcastManager.getInstance(requireContext()).unregisterReceiver(broadcastReceiver)
        LocalBroadcastManager.getInstance(requireContext()).unregisterReceiver(broadcastReceiver2)
    }

    //브로드 캐스트 리시버로 받은 초를 시/분/초로 변환
    private fun formatTime(seconds: Long): String {
        val hours = seconds / 3600
        val minutes = (seconds % 3600) / 60
        val remainingSeconds = seconds % 60
        return String.format("%02d : %02d : %02d", hours, minutes, remainingSeconds)
    }

    //바텀 네비 삭제 메서드
    private fun removeBottomNavi() {
        controlActivityInterface = activity as MainActivity
        controlActivityInterface.goneNavigationBar()
    }

    //시작, 끝 사진 첨부 리사이클러뷰 어댑터 초기화 및 연결 메서드
    private fun initRecyclerviewAdapter() {
        certificationStartImageAdapter = CertificationImageAdapter { itemPosition ->
            viewModel.removeStartImage(itemPosition)
        }

        binding.recyclerCertificateStartImage.apply {
            adapter = certificationStartImageAdapter
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        }

        certificationEndImageAdapter = CertificationImageAdapter { itemPosition ->
            viewModel.removeEndImage(itemPosition)
        }

        binding.recyclerCertificateEndImage.apply {
            adapter = certificationEndImageAdapter
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        }
    }

    //인증 시작 사진 첨부 클릭 리스너
    private fun setStartImageAddButtonClick() {
        binding.imageViewFitStartSelectImage.setOnClickListener {
            if ((viewModel.startImageList.value?.size ?: 0) >= IMAGE_PICK_MAX) {
                return@setOnClickListener
            }
            requestPermission()
        }
    }

    //인증 종료 사진 첨부 클릭 리스너
    private fun setEndImageAddButtonClick() {
        binding.imageViewFitEndSelectImage.setOnClickListener {
            if ((viewModel.endImageList.value?.size ?: 0) >= IMAGE_PICK_MAX) {
                return@setOnClickListener
            }
            pickMultipleMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }
    }

    //사진 첨부 런처
    private fun activityResultLauncher() =
        registerForActivityResult(ActivityResultContracts.PickMultipleVisualMedia()) { uris ->
            if (uris.isNotEmpty()) {
                if (uris.size > IMAGE_PICK_MAX - (viewModel.startImageList.value?.size
                        ?: 0) && viewModel.state.value != CertificateState.PROCEEDING
                ) {
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.certificate_scr_image_add_warning),
                        Toast.LENGTH_SHORT
                    )
                        .show()
                    return@registerForActivityResult
                }
                if (uris.size > IMAGE_PICK_MAX - (viewModel.endImageList.value?.size
                        ?: 0) && viewModel.state.value == CertificateState.PROCEEDING
                ) {
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.certificate_scr_image_add_warning),
                        Toast.LENGTH_SHORT
                    )
                        .show()
                    return@registerForActivityResult
                }
                uris.forEach {
                    if (viewModel.state.value != CertificateState.PROCEEDING) {
                        val image = CertificationImage(it)
                        viewModel.addStartImage(image)
                    } else {
                        val image = CertificationImage(it)
                        viewModel.addEndImage(image)
                    }

                }
            }
        }


    //시작 사진 첨부 수정 불가능하도록 설정하는 메서드
    private fun setRecyclerViewState(visible: Boolean) {
        certificationStartImageAdapter.changeVisible(visible)
    }

    private fun certificationReset() {
        viewModel.deleteCertificationInfo()
    }

    //12시간 초과시 프래그먼트 닫기
    fun closeFragment(state: Boolean) {
        if (state) {
            findNavController().popBackStack()
        }
    }


    //알림 권한 교육용 팝업
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    fun showNotificationPermissionDialog() {
        MaterialAlertDialogBuilder(requireContext(), R.style.Theme_Fitmate_Dialog)
            .setTitle(getString(R.string.certificate_scr_dialog_title))
            .setMessage(getString(R.string.certificate_scr_dialog_message))
            .setPositiveButton(getString(R.string.certificate_scr_dialog_positive_button)) { dialogInterface: DialogInterface, i: Int ->
                navigateToAppSetting()
            }
            .setNegativeButton(getString(R.string.certificate_scr_dialog_negative_button)) { dialogInterface: DialogInterface, i: Int ->
                viewModel.insertCertificateInitInfo()
            }.show()
    }

    private val multiplePermissionsLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        permissions.entries.forEach { (permission, isGranted) ->
            when (permission) {
                Manifest.permission.READ_EXTERNAL_STORAGE -> {
                    if (isGranted) {
                        pickMultipleMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                    } else {
                        if (!shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE)) {
                            showPermissionSettiongDialog()
                        } else {
                            showStoragePermissionDialog()
                        }
                    }
                }

                Manifest.permission.READ_MEDIA_IMAGES -> {
                    if (isGranted) {
                        pickMultipleMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                    } else {
                        if (!shouldShowRequestPermissionRationale(Manifest.permission.READ_MEDIA_IMAGES)) {
                            showPermissionSettiongDialog()
                        } else {
                            showPermissionSettiongDialog()
                        }
                    }
                }
            }
        }
    }

    private fun showSelectCertificateGroup(groupList: List<FitGroupItem>) {
        val dataList = groupList.map { it.fitGroupName }.toTypedArray()
        val multiChoiceList = BooleanArray(dataList.size) { i -> false }
        val resultGroupIdList = mutableListOf<String>()
        val builder = MaterialAlertDialogBuilder(requireContext(), R.style.Theme_Fitmate_Dialog)
        builder.setTitle("인증을 수행할 그룹을 선택해주세요")

        builder.setMultiChoiceItems(
            dataList,
            multiChoiceList
        ) { dialogInterface: DialogInterface, i: Int, b: Boolean ->
            multiChoiceList[i] = b
        }

        builder.setNegativeButton("취소", null)
        builder.setPositiveButton("확인") { dialogInterface: DialogInterface, i: Int ->
            for (idx in 0 until multiChoiceList.size) {
                if (multiChoiceList[idx] == true) {
                    resultGroupIdList.add(groupList[idx].fitGroupId)
                }
            }
            if (resultGroupIdList.size > 0){
                //최종 통신까지 진행 시작
                val intent = Intent(
                    this@CertificateFragment.context,
                    StopWatchService::class.java
                ).apply {
                    action = STOP_WATCH_RESET
                }
                requireContext().startService(intent)
                viewModel.selectedTarget = resultGroupIdList
                networkState = NetworkState.STATE_UPLOAD_STORAGE
                loadingTaskSettingStart()
                viewModel.updateCertificationInfo(
                    viewModel.certificationData.value!!.copy(
                        recordEndDate = Instant.now(),
                        endImages = viewModel.endImageList.value?.map { it.imagesUri }
                            ?.toMutableList(),
                        certificateTime = totalElapsedTime)
                )
                Toast.makeText(requireContext(), "${resultGroupIdList}에 기록합니다.", Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(requireContext(), "그룹을 하나 이상 선택하셔야합니다!", Toast.LENGTH_SHORT).show()
            }
        }
        builder.show()
    }

    private fun showStoragePermissionDialog() {
        MaterialAlertDialogBuilder(requireContext(), R.style.Theme_Fitmate_Dialog)
            .setTitle(getString(R.string.permission_dialog_scr_guide))
            .setMessage(getString(R.string.permission_dialog_scr_guide_message))
            .setPositiveButton(getString(R.string.permission_dialog_scr_guide_select)) { dialogInterface: DialogInterface, i: Int ->
                //권한 물어보기
                requestPermission()
            }
            .setNegativeButton(getString(R.string.permission_dialog_scr_guide_cancel)) { dialogInterface: DialogInterface, i: Int ->
                dialogInterface.cancel()
            }.show()
    }


    //권한 설정 화면을 위한 다이얼로그 띄우는 메서드
    private fun showPermissionSettiongDialog() {
        MaterialAlertDialogBuilder(requireContext(), R.style.Theme_Fitmate_Dialog)
            .setMessage(getString(R.string.permission_dialog_scr_guide_setting))
            .setPositiveButton(getString(R.string.permission_dialog_scr_guide_setting_select)) { dialogInterface: DialogInterface, i: Int ->
                navigateToAppSetting()
            }
            .setNegativeButton(getString(R.string.permission_dialog_scr_guide_setting_cancel)) { dialogInterface: DialogInterface, i: Int ->
                dialogInterface.cancel()
            }.show()
    }

    //앱 권한 세팅 화면으로 이동키시는 메서드
    private fun navigateToAppSetting() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
            data = Uri.fromParts("package", requireContext().packageName, null)
        }
        startActivity(intent)
    }

    //권한 확인 및 요청 메서드
    private fun requestPermission() {
        if (
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
            (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.READ_MEDIA_IMAGES
            ) == PermissionChecker.PERMISSION_GRANTED)
        ) {
            pickMultipleMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        } else if (
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE
            && ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.READ_MEDIA_IMAGES
            ) == PermissionChecker.PERMISSION_DENIED
        ) {
            // 34이상이고 READ_MEDIA_VISUAL_USER_SELECTED만 허용되어있다면 권한 물어보는 다이얼로그를 띄워야함.
            /*showPermissionDialog()*/
            multiplePermissionsLauncher.launch(arrayOf(Manifest.permission.READ_MEDIA_IMAGES))
        } else if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PermissionChecker.PERMISSION_GRANTED
        ) {
            pickMultipleMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        } else {
            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.S_V2) {
                //READ_EXTERNAL_STORAGE 권한 요청
                multiplePermissionsLauncher.launch(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE))
            } else {
                multiplePermissionsLauncher.launch(arrayOf(Manifest.permission.READ_MEDIA_IMAGES))
            }
        }
    }

    private fun loadingTaskSettingStart() {
        binding.apply {
            constraintLayoutRootContent.alpha = 0.5f
            buttonCertificateConfirm.isEnabled = false
            buttonCertificateReset.isEnabled = false
            progressBarSubmitLoading.visibility = View.VISIBLE
        }
    }

    private fun loadingTaskSettingEnd() {
        binding.apply {
            constraintLayoutRootContent.alpha = 1f
            buttonCertificateConfirm.isEnabled = true
            buttonCertificateReset.isEnabled = true
            progressBarSubmitLoading.visibility = View.GONE
        }
    }


}