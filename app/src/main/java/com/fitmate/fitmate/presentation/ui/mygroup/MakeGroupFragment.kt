package com.fitmate.fitmate.presentation.ui.mygroup

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
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.fitmate.fitmate.MainActivity
import com.fitmate.fitmate.R
import com.fitmate.fitmate.databinding.FragmentMakeGroupBinding
import com.fitmate.fitmate.domain.model.BankList
import com.fitmate.fitmate.domain.model.CertificationImage
import com.fitmate.fitmate.domain.model.RequestRegisterFitGroupBody
import com.fitmate.fitmate.presentation.ui.mygroup.list.adapter.BankListAdapter
import com.fitmate.fitmate.presentation.ui.mygroup.list.adapter.MakeGroupImageAdapter
import com.fitmate.fitmate.presentation.viewmodel.MakeGroupViewModel
import com.fitmate.fitmate.util.ControlActivityInterface
import com.fitmate.fitmate.util.GroupCategory
import com.fitmate.fitmate.util.readData
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetBehavior.STATE_COLLAPSED
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.slider.Slider
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MakeGroupFragment : Fragment() {
    companion object {
        //사진 최대 선택 가능 갯수
        private const val IMAGE_PICK_MAX = 5
    }

    private lateinit var binding: FragmentMakeGroupBinding
    private lateinit var controlActivityInterface: ControlActivityInterface
    private val viewModel: MakeGroupViewModel by viewModels()
    private var pickMultipleMedia = activityResultLauncher()
    private lateinit var imageListAdapter: MakeGroupImageAdapter
    private var userId: Int = -1
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentMakeGroupBinding.inflate(layoutInflater)
        binding.seekBarMakeGroupFitNum.setCustomThumbDrawable(R.drawable.slider_thunb)
        binding.seekBarMakeGroupPeople.setCustomThumbDrawable(R.drawable.slider_thunb)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.fragmentMakeGroup = this
        binding.viewModel = viewModel

        controlActivityInterface = activity as MainActivity
        controlActivityInterface.goneNavigationBar()

        val userPreference = (activity as MainActivity).loadUserPreference()
        userId = userPreference.getOrNull(2)?.toString()?.toInt() ?: -1

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.materialToolbar2.setupWithNavController(findNavController())

        //이미지 변동사항을 감시해서 리사이클러뷰 업데이트(추가 및 삭제)
        observeImageChange()

        //이미지 스토리지 업로드 결과 감시
        observeUploadImageToStorage()

        //post 작업 감시
        observePostMakeGroupRegister()


        //이미지 추가 리사이클러뷰 설정
        setImageListRecyclerView()

        //카테고리 선택 변동 리스너 설정
        settingCategorySelectListener()

        //시크바 리스너 설정(주 운동 횟수, 최대 인원 수)
        settingSeekBarListener()
    }

    private fun observePostMakeGroupRegister() {
        viewModel.postResult.observe(viewLifecycleOwner) {
            //로딩 종료
            loadingViewGone()
        }
    }

    private fun observeImageChange() {
        viewModel.groupImageList.observe(viewLifecycleOwner) {
            imageListAdapter.submitList(it)
            imageListAdapter.notifyDataSetChanged()
        }
    }

    //그룹 이미지 리스트 라시이클러뷰 설정
    private fun setImageListRecyclerView() {
        imageListAdapter = MakeGroupImageAdapter {
            viewModel.removeImage(it)
        }
        binding.recyclerGroupImage.apply {
            adapter = imageListAdapter
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        }
    }

    private fun settingCategorySelectListener() {
        binding.editTextCategorySelect.doAfterTextChanged {
            Log.d("testt", "카테고리는 : ${it.toString()}")
            viewModel.setCategorySelect(it.toString())
        }
    }

    private fun settingSeekBarListener() {
        binding.seekBarMakeGroupFitNum.addOnSliderTouchListener(object :
            Slider.OnSliderTouchListener {
            override fun onStartTrackingTouch(slider: Slider) {
                hideKeyboard()
            }

            override fun onStopTrackingTouch(slider: Slider) {
                viewModel.setGroupFitCycle(slider.value.toInt())
            }
        })

        binding.seekBarMakeGroupPeople.addOnSliderTouchListener(object :
            Slider.OnSliderTouchListener {
            override fun onStartTrackingTouch(slider: Slider) {
                hideKeyboard()
            }

            override fun onStopTrackingTouch(slider: Slider) {
                viewModel.setGroupFitMateLimit(slider.value.toInt())
            }
        })
    }



    fun onClickAddImageButton() {
        if ((viewModel.groupImageList.value?.size ?: 0) >= IMAGE_PICK_MAX) {
            Toast.makeText(requireContext(),getString(R.string.certificate_scr_image_add_warning),Toast.LENGTH_SHORT).show()
            return
        }
        requestPermission()
    }


    //그룹 만들기 버튼 클릭 시 리스너 설정
    fun onclickConfirmButton() {
        //키모드 내리기
        hideKeyboard()

        //유효성 검사
        if (!checkInputValidation()) return

        //사진 업로드 및 url 다운로드
        viewModel.uploadImageAndGetUrl(userId.toString())

        //로딩 실행
        loadingViewVisible()
    }

    private fun loadingViewVisible() {
        binding.loadingLayoutView.apply {
            visibility = View.VISIBLE
            alpha = 0.5f
            isClickable = true
        }

        binding.progressBarSubmitLoading.visibility = View.VISIBLE
    }

    //입력 값 유효성 겅사
    private fun checkInputValidation(): Boolean {
        when {
            viewModel.groupName.value.isNullOrBlank() -> {
                Toast.makeText(requireContext(),"그룹 이름을 입력해주세요!",Toast.LENGTH_SHORT).show()
                return false
            }
            viewModel.groupCategory.value.isNullOrBlank() -> {
                Toast.makeText(requireContext(),"그룹 카테고리를 선택헤주세요ㅕ",Toast.LENGTH_SHORT).show()
                return false
            }
            viewModel.groupFitCycle.value == 0 -> {
                Toast.makeText(requireContext(),"운동횟수(운동 주기)가 0 이하입니다!",Toast.LENGTH_SHORT).show()
                return false
            }
            viewModel.groupFitMateLimit.value == 0 -> {
                Toast.makeText(requireContext(),"최대 인원 수가 0 이하입니다!",Toast.LENGTH_SHORT).show()
                return false
            }
            viewModel.groupContent.value.isNullOrBlank() -> {
                Toast.makeText(requireContext(),"상세 설명을 입력해주세요!",Toast.LENGTH_SHORT).show()
                return false
            }
            viewModel.groupImageList.value.isNullOrEmpty() -> {
                Toast.makeText(requireContext(),"그룹 사진을 한 장 이상 첨부해주세요!",Toast.LENGTH_SHORT).show()
                return false
            }
            else -> { return true }
        }
    }

    private fun observeUploadImageToStorage(){
        viewModel.groupImageUrlList.observe(viewLifecycleOwner){urls ->
            //TODO post 작업 수행
            val postData = RequestRegisterFitGroupBody(
                requestUserId = userId,
                fitGroupName = viewModel.groupName.value.toString(),
                penaltyAmount = 5000L,
                cycle = null,
                category = getCategoryCode(),
                introduction = viewModel.groupContent.value!!,
                frequency = viewModel.groupFitCycle.value!!,
                maxFitMate = viewModel.groupFitMateLimit.value!!,
                multiMediaEndPoints = urls
            )
            viewModel.postRegisterFitGroup(postData)
        }
    }

    private fun getCategoryCode() = when (viewModel.groupCategory.value.toString()) {
            requireContext().getString(R.string.category_scr_1) -> GroupCategory.CLIMBING.code
            requireContext().getString(R.string.category_scr_2) -> GroupCategory.LIFE_SPORTS.code
            requireContext().getString(R.string.category_scr_3) -> GroupCategory.WEIGHT_TRAINING.code
            requireContext().getString(R.string.category_scr_4) -> GroupCategory.SWIMMING.code
            requireContext().getString(R.string.category_scr_5) -> GroupCategory.SOCCER.code
            requireContext().getString(R.string.category_scr_6) -> GroupCategory.BASKETBALL.code
            requireContext().getString(R.string.category_scr_7) -> GroupCategory.BASEBALL.code
            requireContext().getString(R.string.category_scr_8) -> GroupCategory.BIKING.code
            requireContext().getString(R.string.category_scr_9) -> GroupCategory.CLIMBING.code
            else -> throw IllegalArgumentException("Invalid category")
        }


    private fun loadingViewGone() {
        binding.loadingLayoutView.apply {
            visibility = View.GONE
            alpha = 1f
            isClickable = false
        }

        binding.progressBarSubmitLoading.visibility = View.GONE
    }

    private fun activityResultLauncher() =
        registerForActivityResult(ActivityResultContracts.PickMultipleVisualMedia()) { uris ->
            if (uris.isNotEmpty()) {
                if (uris.size > IMAGE_PICK_MAX - (viewModel.groupImageList.value?.size
                        ?: 0)) {
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.certificate_scr_image_add_warning),
                        Toast.LENGTH_SHORT
                    )
                        .show()
                    return@registerForActivityResult
                }
                uris.forEach {
                    val image = CertificationImage(it)
                    viewModel.addImage(image)
                }
            }
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

    //권한 결과 확인 메서드
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
                            showPermissionSettingDialog()
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
    fun hideKeyboard() {
        if (activity?.currentFocus != null){
            val imm = activity?.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(activity?.currentFocus!!.windowToken, 0)
        }
    }
}