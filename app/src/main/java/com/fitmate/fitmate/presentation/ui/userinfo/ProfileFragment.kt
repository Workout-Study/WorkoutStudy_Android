package com.fitmate.fitmate.presentation.ui.userinfo

import android.Manifest
import android.content.Context.INPUT_METHOD_SERVICE
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.content.PermissionChecker
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.fitmate.fitmate.MainActivity
import com.fitmate.fitmate.R
import com.fitmate.fitmate.data.model.dto.UserResponse
import com.fitmate.fitmate.databinding.FragmentProfileBinding
import com.fitmate.fitmate.presentation.viewmodel.LoginViewModel
import com.fitmate.fitmate.presentation.viewmodel.UserUpdateViewModel
import com.fitmate.fitmate.util.ControlActivityInterface
import com.fitmate.fitmate.util.customGetSerializable
import com.fitmate.fitmate.util.setImageByUrl
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding
    private val viewModel: UserUpdateViewModel by viewModels()
    private lateinit var ownerNameInfo: UserResponse
    private var pickMedia = activityResultLauncher()
    private var userId: Int = -1
    private var accessToken: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let{
            it.customGetSerializable<UserResponse>("userInfoData")?.let{
                ownerNameInfo = it
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //번들 데이터가 초기화 되었을 경우만 해당 프래그먼트 수행되도록 분기를 나눔
        return if (::ownerNameInfo.isInitialized){
            binding = FragmentProfileBinding.inflate(layoutInflater)
            binding.fragment = this
            binding.myInfoData = ownerNameInfo
            binding.viewModel = viewModel

            viewModel.updateUserImage.value = ownerNameInfo.imageUrl

            binding.root
        }else{
            Toast.makeText(requireContext(),"네트워크 오류로 인해 프로필 변경창을 종료합니다",Toast.LENGTH_SHORT).show()
            findNavController().popBackStack()
            return null
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as ControlActivityInterface).goneNavigationBar()
        loadUserPreference()
        binding.toolbarProfile.setupWithNavController(findNavController())

        viewModel.isImageUpload.observe(viewLifecycleOwner) {
            Log.d("tlqkf","업로드된 url:$it")
            if (it != null && viewModel.updateUserNickName.value != null){
                viewModel.updateUserInfo(userToken = accessToken, viewModel.updateUserNickName.value!!, it.toString())
            }else{
                Toast.makeText(requireContext(),"알 수 없는 오류로 사진 업로드에 실패했습니다!",Toast.LENGTH_SHORT).show()
                loadingViewGone()
            }
        }

        viewModel.success.observe(viewLifecycleOwner) {
            loadingViewGone()
            Toast.makeText(requireContext(),"프로필 변경이 완료되었습니다",Toast.LENGTH_SHORT).show()
            //TODO 뒤로가기 후 해당 화면은 업데이트 되어야함.
        }
        viewModel.updateUserImage.observe(viewLifecycleOwner) {
            binding.imageViewProfliePhoto.setImageByUrl(it)
        }
    }

    //Preference값 가져오기
    private fun loadUserPreference() {
        val userPreference = (activity as MainActivity).loadUserPreference()
        userId = userPreference.getOrNull(2)?.toString()?.toInt() ?: -1
        accessToken = userPreference.getOrNull(0)?.toString() ?: ""
    }

    //프로필 변경 클릭 시
    fun clickUpdateMyInfo() {
        if (viewModel.updateUserImage.value == ownerNameInfo.imageUrl && (viewModel.updateUserNickName.value == null || viewModel.updateUserNickName.value == binding.editTextSetProfileName.hint || viewModel.updateUserNickName.value == "")){
            Toast.makeText(requireContext(),"프로필 사진이나 닉네임의 변경사항이 발견되지 않았습니다!",Toast.LENGTH_SHORT).show()
        }else{
            if(viewModel.updateUserImage.value == ownerNameInfo.imageUrl && (viewModel.updateUserNickName.value != null || viewModel.updateUserNickName.value != binding.editTextSetProfileName.hint || viewModel.updateUserNickName.value != "")) {
                //사진은 변경안됐고 닉네임의 변경사항이 있을 경우
                //TODO 사진 업로드 없이 닉네임만 수정해서 업데이트 수행(기존 사진은 그대로 던지기)
                //loadingViewVisible()
                
            }else if (viewModel.updateUserImage.value != ownerNameInfo.imageUrl &&(viewModel.updateUserNickName.value == null || viewModel.updateUserNickName.value == binding.editTextSetProfileName.hint || viewModel.updateUserNickName.value == "")){
                //사진은 변경됐고 닉네임의 변경사항은 없을 경우
                //TODO 사진 업로드 수행하고 닉네임은 hint로 보내기
                viewModel.updateUserImage.value?.let {
                    //TODO 로딩 화면 시작시키기
                    //loadingViewVisible()
                    //viewModel.imageUpload(userId = userId, it.toUri())
                }
            }else{
                //사진과 닉네임 전부 변경했을 경우
                //TODO 둘다 변경해서 보내기
                viewModel.updateUserImage.value?.let {
                    //TODO 로딩 화면 시작시키기
                    //loadingViewVisible()
                    //viewModel.imageUpload(userId = userId, it.toUri())
                }
            }


        }
    }

    //이미지 변경 버튼 클릭시
    fun clickChangeMyImageImage() {
        hideKeyboard()
        requestPermission()
    }

    private fun activityResultLauncher() =
        registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uris ->
            if (uris != null) {
                //변경될 사진 viewModel 라이브데이터 값에 전달
                viewModel.updateUserImage.value = uris.toString()
            }else{
                return@registerForActivityResult
            }
        }

    private fun loadingViewVisible() {
        binding.loadingLayoutView.apply {
            visibility = View.VISIBLE
            alpha = 0.5f
            isClickable = true
        }

        binding.progressBarSubmitLoading.visibility = View.VISIBLE
    }

    private fun loadingViewGone() {
        binding.loadingLayoutView.apply {
            visibility = View.GONE
            alpha = 1f
            isClickable = false
        }

        binding.progressBarSubmitLoading.visibility = View.GONE
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
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
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
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        } else {
            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.S_V2) {
                //READ_EXTERNAL_STORAGE 권한 요청
                multiplePermissionsLauncher.launch(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE))
            } else {
                multiplePermissionsLauncher.launch(arrayOf(Manifest.permission.READ_MEDIA_IMAGES))
            }
        }
    }

    //권한 결과 확인 메서드
    private val multiplePermissionsLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        permissions.entries.forEach { (permission, isGranted) ->
            when (permission) {
                Manifest.permission.READ_EXTERNAL_STORAGE -> {
                    if (isGranted) {
                        pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                    } else {
                        if (!shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE)) {
                            showPermissionSettingDialog()
                        } else {
                            showStoragePermissionDialog()
                        }
                    }
                }

                Manifest.permission.READ_MEDIA_IMAGES -> {
                    if (isGranted) {
                        pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                    } else {
                        if (!shouldShowRequestPermissionRationale(Manifest.permission.READ_MEDIA_IMAGES)) {
                            showPermissionSettingDialog()
                        } else {
                            showPermissionSettingDialog()
                        }
                    }
                }
            }
        }
    }

    //교육용 다이얼로그
    private fun showStoragePermissionDialog() {
        MaterialAlertDialogBuilder(requireContext())
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

    //권한 설정 창으로 이동시키기 위한 다이얼로그
    private fun showPermissionSettingDialog() {
        MaterialAlertDialogBuilder(requireContext())
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

    //키보드 내리고 포커스 해제
    private fun hideKeyboard() {
        if (activity?.currentFocus != null){
            val imm = activity?.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(activity?.currentFocus!!.windowToken, 0)
        }
    }
}
