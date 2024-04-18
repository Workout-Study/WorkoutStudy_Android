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
import com.fitmate.fitmate.presentation.ui.mygroup.list.adapter.BankListAdapter
import com.fitmate.fitmate.presentation.ui.mygroup.list.adapter.MakeGroupImageAdapter
import com.fitmate.fitmate.presentation.viewmodel.MakeGroupViewModel
import com.fitmate.fitmate.util.ControlActivityInterface
import com.fitmate.fitmate.util.readData
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetBehavior.STATE_COLLAPSED
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.slider.Slider

class MakeGroupFragment : Fragment(R.layout.fragment_make_group) {
    companion object {
        //사진 최대 선택 가능 갯수
        private const val IMAGE_PICK_MAX = 5
    }

    private lateinit var binding: FragmentMakeGroupBinding
    private lateinit var controlActivityInterface: ControlActivityInterface
    private val viewModel: MakeGroupViewModel by viewModels()
    private val bottomSheetBehavior by lazy { BottomSheetBehavior.from(binding.bankBottomSheetLayout.root) }
    private var pickMultipleMedia = activityResultLauncher()
    private lateinit var imageListAdapter: MakeGroupImageAdapter
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentMakeGroupBinding.inflate(layoutInflater)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.fragmentMakeGroup = this
        binding.viewModel = viewModel

        controlActivityInterface = activity as MainActivity
        controlActivityInterface.goneNavigationBar()

        bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.materialToolbar2.setupWithNavController(findNavController())

        //이미지 변동사항을 감시해서 리사이클러뷰 업데이트(추가 및 삭제)
        observeImageChange()

        //바텀 시트 리사이클러뷰 설정(은행)
        settingBottomSheetAdapter()

        //이미지 추가 리사이클러뷰 설정
        setImageListRecyclerView()

        //카테고리 선택 변동 리스너 설정
        settingCategorySelectListener()

        //시크바 리스너 설정(주 운동 횟수, 최대 인원 수)
        settingSeekBarListener()
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

    private fun settingBottomSheetAdapter() {
        binding.bankBottomSheetLayout.recyclerViewBottomSheetBankList.apply {
            val bankList = requireContext().readData("bank.json", BankList::class.java) ?: BankList(
                emptyList()
            )
            setHasFixedSize(true)
            layoutManager = GridLayoutManager(requireContext(), 3)
            adapter = BankListAdapter(bankList.banks) {
                viewModel.setBankInfo(it)
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
            }
        }
    }

    fun onClickAddImageButton() {
        if ((viewModel.groupImageList.value?.size ?: 0) >= IMAGE_PICK_MAX) {
            Toast.makeText(requireContext(),getString(R.string.certificate_scr_image_add_warning),Toast.LENGTH_SHORT).show()
            return
        }
        requestPermission()
    }
    fun collapseBottomSheet() {
        hideKeyboard()
        bottomSheetBehavior.state = STATE_COLLAPSED
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
    private fun showPermissionSettiongDialog() {
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

    fun hideKeyboard() {
        if (activity?.currentFocus != null){
            val imm = activity?.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(activity?.currentFocus!!.windowToken, 0)
            activity?.currentFocus!!.clearFocus()
        }
    }
}