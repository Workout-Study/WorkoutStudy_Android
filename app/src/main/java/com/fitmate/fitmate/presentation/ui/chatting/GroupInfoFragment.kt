package com.fitmate.fitmate.presentation.ui.chatting

import android.Manifest
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.content.PermissionChecker
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.fitmate.fitmate.MainActivity
import com.fitmate.fitmate.R
import com.fitmate.fitmate.data.model.dto.GetFitGroupDetail
import com.fitmate.fitmate.databinding.FragmentGroupInfoBinding
import com.fitmate.fitmate.domain.model.CertificationImage
import com.fitmate.fitmate.presentation.ui.mygroup.MakeGroupFragment
import com.fitmate.fitmate.presentation.ui.mygroup.list.adapter.MakeGroupImageAdapter
import com.fitmate.fitmate.presentation.viewmodel.UpdateGroupViewModel
import com.fitmate.fitmate.util.customGetSerializable
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.slider.Slider
import com.google.android.material.textfield.MaterialAutoCompleteTextView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GroupInfoFragment : Fragment(R.layout.fragment_group_info) {

    private lateinit var binding: FragmentGroupInfoBinding
    private val viewModel: UpdateGroupViewModel by viewModels()
    private var groupInfoData: GetFitGroupDetail? = null
    private var pickMultipleMedia = activityResultLauncher()
    private lateinit var imageListAdapter: MakeGroupImageAdapter
    private var userId: Int = -1
    private var updateState = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        groupInfoData = arguments?.customGetSerializable<GetFitGroupDetail>("groupDetailData")
        if (groupInfoData == null) {
            Toast.makeText(requireContext(), "통신 오류로 화면을 전환합니다", Toast.LENGTH_SHORT).show()
            findNavController().popBackStack()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView(view)//초기 설정
        setImageListRecyclerView() //사진 리사이클러뷰 설정
        loadUserPreferenceAndSetting()//내 아이디 가져오고 수정 버튼 visible 설정
        observeImageChange()
    }

    private fun initView(view: View) {
        binding = FragmentGroupInfoBinding.bind(view)
        binding.viewModel = viewModel
        binding.fragment = this
        binding.materialToolbar.setupWithNavController(findNavController())
        viewModel.bindData(groupInfoData!!) //라이브 데이터에 값 전달

        //시크바 value설정
        binding.seekBarMakeGroupFitNum.value = groupInfoData!!.frequency.toFloat()
        binding.seekBarMakeGroupPeople.value = groupInfoData!!.maxFitMate.toFloat()

        //시크바로 변뎡되는 값을 감시하여 guide textView값 설정
        viewModel.groupFitCycle.observe(viewLifecycleOwner) {
            binding.textViewMakeGroupFitNum.text =  requireContext().getString(R.string.make_group_scr_fit_num, it)
        }
        viewModel.groupFitMateLimit.observe(viewLifecycleOwner) {
            binding.textViewMakeGroupPeople.text =  requireContext().getString(R.string.make_group_scr_people, it)
        }

        //시크바 change 리스너 설정
        binding.seekBarMakeGroupFitNum.addOnSliderTouchListener(object :
            Slider.OnSliderTouchListener {
            override fun onStartTrackingTouch(slider: Slider) {}
            override fun onStopTrackingTouch(slider: Slider) {
                viewModel.setGroupFitCycle(slider.value.toInt())
            }
        })

        binding.seekBarMakeGroupPeople.addOnSliderTouchListener(object :
            Slider.OnSliderTouchListener {
            override fun onStartTrackingTouch(slider: Slider) {}
            override fun onStopTrackingTouch(slider: Slider) {
                viewModel.setGroupFitMateLimit(slider.value.toInt())
            }
        })

        //최초엔 컨텐츠 disable 상태로 설정
        contentDisable()
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

    private fun observeImageChange() {
        viewModel.groupImageList.observe(viewLifecycleOwner) {
            imageListAdapter.submitList(it)
            imageListAdapter.notifyDataSetChanged()
        }
    }


    private fun loadUserPreferenceAndSetting() {
        val userPreference = (activity as MainActivity).loadUserPreference()
        userId = userPreference.getOrNull(2)?.toString()?.toInt() ?: -1

        if (userId.toString() == groupInfoData?.fitLeaderUserId) {
            binding.imageViewUpdateGroupInfo.visibility = View.VISIBLE
            binding.imageViewUpdateGroupInfo.setOnClickListener {
                if (updateState) return@setOnClickListener
                contentEnable()
            }
        } else {
            binding.imageViewUpdateGroupInfo.visibility = View.GONE
        }
    }

    private fun contentEnable() {
        updateState = true
        binding.apply {
            editTextSetGroupName.isEnabled = true //이름

            editTextCategorySelect.isEnabled = true //카테고리
            (editTextCategorySelect as? MaterialAutoCompleteTextView)?.setSimpleItems(R.array.group_category)

            seekBarMakeGroupFitNum.isEnabled = true //빈도

            seekBarMakeGroupPeople.isEnabled = true //최대 인원

            editTextSetGroupComment.isEnabled = true //코멘트

            cardViewItemCertificateStart.visibility = View.VISIBLE //카드뷰

            buttonMakeGroupConfirm.visibility = View.VISIBLE //수정 완료 버튼
        }
    }

    private fun contentDisable() {
        updateState = false
        binding.apply {
            editTextSetGroupName.isEnabled = false //이름

            editTextCategorySelect.isEnabled = false //카테고리

            seekBarMakeGroupFitNum.isEnabled = false //빈도

            seekBarMakeGroupPeople.isEnabled = false //최대 인원

            editTextSetGroupComment.isEnabled = false //코멘트

            cardViewItemCertificateStart.visibility = View.GONE //카드뷰

            buttonMakeGroupConfirm.visibility = View.GONE //수정 완료 버튼
        }
    }

    fun onClickAddImageButton() {
        if ((viewModel.groupImageList.value?.size ?: 0) >= 5) {
            Toast.makeText(requireContext(),getString(R.string.certificate_scr_image_add_warning),Toast.LENGTH_SHORT).show()
            return
        }
        requestPermission()
    }


    private fun activityResultLauncher() =
        registerForActivityResult(ActivityResultContracts.PickMultipleVisualMedia()) { uris ->
            if (uris.isNotEmpty()) {
                if (uris.size > 5 - (viewModel.groupImageList.value?.size
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



}