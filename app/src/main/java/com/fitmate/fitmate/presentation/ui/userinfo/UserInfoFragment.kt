package com.fitmate.fitmate.presentation.ui.userinfo

import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.Manifest.permission.READ_MEDIA_IMAGES
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.PermissionChecker.PERMISSION_DENIED
import androidx.core.content.PermissionChecker.PERMISSION_GRANTED
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.fitmate.fitmate.MainActivity
import com.fitmate.fitmate.R
import com.fitmate.fitmate.databinding.FragmentUserInfoBinding
import com.fitmate.fitmate.presentation.viewmodel.LoginViewModel
import com.fitmate.fitmate.util.ControlActivityInterface
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UserInfoFragment : Fragment(R.layout.fragment_user_info) {

    private lateinit var binding: FragmentUserInfoBinding
    private val viewModel: LoginViewModel by viewModels()
    private val TAG = "UserInfoFragment"
    private var userId: Int = -1
    private var accessToken: String = ""
    private var platform: String = ""
    private val multiplePermissionsLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
        permissions.entries.forEach { (permission, isGranted) ->
            handlePermissionResult(permission, isGranted)
        }
    }

    private fun handlePermissionResult(permission: String, isGranted: Boolean) {
        if (isGranted) {
            accessGallery()
        } else {
            if (!shouldShowRequestPermissionRationale(permission)) {
                showPermissionSettingDialog()
            } else {
                showPermissionDialog()
            }
        }
    }

    private val pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        if (uri != null) showImagePreview(uri)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentUserInfoBinding.bind(view)

        (activity as ControlActivityInterface).viewNavigationBar()
        loadUserPreference()

        setClickListener()
        loadProfileImage()
    }

    private fun loadUserPreference() {
        val userPreference = (activity as MainActivity).loadUserPreference()
        userId = userPreference.getOrNull(2)?.toString()?.toInt() ?: -1
        accessToken = userPreference.getOrNull(0)?.toString() ?: ""
        platform = userPreference.getOrNull(3)?.toString() ?: ""
        Log.d(TAG, "$userId, $platform")
        Log.d(TAG, accessToken)
    }

    private fun setClickListener() {
        listOf(
            binding.textViewUserInfoProfile,
            binding.textViewUserInfoNickname,
            binding.textViewUserInfoFitOff,
            binding.textViewUserInfoNotice,
            binding.textViewUserInfoOSS,
            binding.textViewUserInfoLogout,
            binding.textViewUserInfoWithDraw
        ).forEach { textView ->
            textView.setOnClickListener { handleOnClick(textView.id) }
        }
    }

    private fun handleOnClick(viewId: Int) {
        when (viewId) {
            R.id.textViewUserInfoProfile -> requestPermission() // TODO 권한 체크로 변경하고 권한 체크하는 곳에서 이동하도록 변경
            R.id.textViewUserInfoNickname -> changeNickname()
            R.id.textViewUserInfoFitOff -> navigateFitOff()
            R.id.textViewUserInfoNotice -> notice()
            R.id.textViewUserInfoOSS -> navigateLicense()
            R.id.textViewUserInfoLogout -> logout()
            R.id.textViewUserInfoWithDraw -> withdraw()
        }
    }

    private fun navigateTo(actionId: Int) { findNavController().navigate(actionId) }

    private fun showImagePreview(uri: Uri) {
        val imagePreviewView = layoutInflater.inflate(R.layout.dialog_user_thumbnail, null)
        val preview = imagePreviewView.findViewById<ImageView>(R.id.imageViewPreview)

        Glide.with(this).load(uri).into(preview)
        MaterialAlertDialogBuilder(requireContext(), R.style.Theme_Fitmate_Dialog)
            .setView(imagePreviewView)
            .setPositiveButton(android.R.string.ok) { dialog, _ ->
                dialog.dismiss()
                Glide.with(this)
                    .load(uri)
                    .apply(RequestOptions().circleCrop())
                    .into(binding.imageViewUserInfoIcon)
                imageUpload(userId, uri)
            }
            .setNegativeButton(android.R.string.cancel) { dialog, _ -> dialog.dismiss() }
            .show()

    }

    private fun imageUpload(userId: Int, uri: Uri) {
        val activity = activity ?: return
        val storage = Firebase.storage
        val storageRef = storage.getReference("user_profile")

        val fileName = "${userId}_profile.png"
        val userRef = storageRef.child(fileName)

        userRef.putFile(uri).addOnSuccessListener {
            userRef.downloadUrl.addOnSuccessListener {
                activity.getSharedPreferences("UserInfo", AppCompatActivity.MODE_PRIVATE).edit()
                    .apply {
                        putString("profileImageUri", uri.toString())
                        apply()
                    }

                Toast.makeText(activity, "성공적으로 프로필 사진을 변경하였습니다.", Toast.LENGTH_SHORT).show()
            }
        }.addOnFailureListener {
            Toast.makeText(activity, "사진을 업로드하는데 실패하였습니다.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun loadProfileImage() {
        val sharedPreferences =
            requireActivity().getSharedPreferences("UserInfo", AppCompatActivity.MODE_PRIVATE)
        val profileImageUri = sharedPreferences.getString("profileImageUri", null)
        profileImageUri?.let {
            Glide.with(this).load(Uri.parse(it)).apply(RequestOptions().circleCrop())
                .into(binding.imageViewUserInfoIcon)
        }
    }

    private fun accessGallery() {
        pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private fun notice() {
        Toast.makeText(context, "지정된 공지사항이 존재하지 않습니다.", Toast.LENGTH_SHORT).show()
    }

    private fun logout() {
        when(platform) {
            "kakao" -> viewModel.logout(accessToken, "kakao")
            "naver" -> viewModel.logout(accessToken, "naver")
        }

        viewModel.logoutComplete.observe(viewLifecycleOwner) { isComplete ->
            if (isComplete) {
                navigateTo(R.id.action_userInfoFragment_to_loginFragment)
                Snackbar.make(binding.root, "로그아웃을 성공했습니다.", Snackbar.LENGTH_SHORT).show()
            }
        }
    }

    private fun withdraw() {
        navigateTo(R.id.action_userInfoFragment_to_loginFragment)
    }

    private fun navigateLicense() {
        navigateTo(R.id.action_userInfoFragment_to_licenseFragment)
    }

    private fun navigateFitOff() {
        Toast.makeText(context, "추후 업데이트 예정입니다.", Toast.LENGTH_SHORT).show()
    }

    private fun changeNickname() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_user_change_nick, null)
        val editTextNewNickname = dialogView.findViewById<EditText>(R.id.editTextNewNickname)

        MaterialAlertDialogBuilder(requireContext(), R.style.Theme_Fitmate_Dialog).apply {
            setView(dialogView)
            setPositiveButton("저장") { _, _ ->
                val newNickname = editTextNewNickname.text.toString().trim()
                Toast.makeText(context, "${newNickname}님 닉네임 변경 사실을 그룹에 알려주세요!", Toast.LENGTH_SHORT).show()
                Log.d("woojugoing_change_nickname", newNickname)
                sendServerToNickname(newNickname)
            }
            setNegativeButton("취소") { dialog, _ -> dialog.cancel() }
        }.show()
    }

    private fun sendServerToNickname(newNickname: String) {
        // TODO 서버에 닉네임 변경 사항을 올려보내는 과정 완료 시 변경되게 if문 추가
        viewModel.updateNickname(1, newNickname)
        binding.textViewUserInfoName.text = newNickname
    }


    //교육용 팝업 띄우는 메서드
    private fun showPermissionDialog() {
        MaterialAlertDialogBuilder(requireContext(), R.style.Theme_Fitmate_Dialog)
            .setTitle(getString(R.string.permission_dialog_scr_guide))
            .setMessage(getString(R.string.permission_dialog_scr_guide_message))
            .setPositiveButton(getString(R.string.permission_dialog_scr_guide_select)) { _, _ ->
                //권한 물어보기
                requestPermission()
            }
            .setNegativeButton(getString(R.string.permission_dialog_scr_guide_cancel)) { dialogInterface: DialogInterface, _ ->
                dialogInterface.cancel()
            }.show()
    }

    //권한 설정 화면을 위한 다이얼로그 띄우는 메서드
    private fun showPermissionSettingDialog() {
        MaterialAlertDialogBuilder(requireContext(), R.style.Theme_Fitmate_Dialog)
            .setMessage(getString(R.string.permission_dialog_scr_guide_setting))
            .setPositiveButton(getString(R.string.permission_dialog_scr_guide_setting_select)) { _, _ ->
                navigateToAppSetting()
            }
            .setNegativeButton(getString(R.string.permission_dialog_scr_guide_setting_cancel)) { dialogInterface: DialogInterface, _ ->
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
    private fun requestPermission(){
        if (
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
            (ContextCompat.checkSelfPermission(requireContext(), READ_MEDIA_IMAGES) == PERMISSION_GRANTED)
        ) {
            accessGallery()
        } else if (
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE
            &&  ContextCompat.checkSelfPermission(requireContext(), READ_MEDIA_IMAGES) == PERMISSION_DENIED
        ) {
            // 34이상이고 READ_MEDIA_VISUAL_USER_SELECTED만 허용되어있다면 권한 물어보는 다이얼로그를 띄워야함.
            /*showPermissionDialog()*/
            multiplePermissionsLauncher.launch(arrayOf(READ_MEDIA_IMAGES))
        }  else if (ContextCompat.checkSelfPermission(requireContext(), READ_EXTERNAL_STORAGE) == PERMISSION_GRANTED) {
            accessGallery()
        } else {
            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.S_V2){
                //READ_EXTERNAL_STORAGE 권한 요청
                multiplePermissionsLauncher.launch(arrayOf(READ_EXTERNAL_STORAGE))
            }else{
                multiplePermissionsLauncher.launch(arrayOf(READ_MEDIA_IMAGES))
            }
        }
    }
}

