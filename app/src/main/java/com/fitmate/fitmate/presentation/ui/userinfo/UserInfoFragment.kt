package com.fitmate.fitmate.presentation.ui.userinfo

import android.Manifest
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.fitmate.fitmate.R
import com.fitmate.fitmate.databinding.FragmentUserInfoBinding
import com.fitmate.fitmate.util.ControlActivityInterface
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import android.provider.Settings

class UserInfoFragment : Fragment(R.layout.fragment_user_info) {

    private lateinit var binding: FragmentUserInfoBinding
    private lateinit var uri: Uri

    private val multiplePermissionsLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
        permissions.entries.forEach { (permission, isGranted) ->
            when(permission){
                Manifest.permission.READ_EXTERNAL_STORAGE ->{
                    if (isGranted){
                        accessGallery()
                    }else{
                        if (!shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE)){
                            showPermissionSettiongDialog()
                        }else{
                            showPermissionDialog()
                        }
                    }
                }
                Manifest.permission.WRITE_EXTERNAL_STORAGE ->{
                    if (isGranted){
                        accessGallery()
                    }else{
                        if (!shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)){
                            showPermissionSettiongDialog()
                        }else{
                            showPermissionDialog()
                        }
                    }
                }
                Manifest.permission.READ_MEDIA_IMAGES -> {
                    if (isGranted){
                        accessGallery()
                    }else{
                        if (!shouldShowRequestPermissionRationale(Manifest.permission.READ_MEDIA_IMAGES)){
                            showPermissionSettiongDialog()
                        }else{
                            showPermissionDialog()
                        }
                    }
                }
                Manifest.permission.READ_MEDIA_VISUAL_USER_SELECTED -> {
                    if (isGranted){
                        accessGallery()
                    }else{
                        if (!shouldShowRequestPermissionRationale(Manifest.permission.READ_MEDIA_IMAGES)){
                            showPermissionSettiongDialog()
                        }else{
                            showPermissionDialog()
                        }
                    }
                }
            }
        }
    }

    private val registerForActivityResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            when (result.resultCode) {
                AppCompatActivity.RESULT_OK -> {
                    uri = result.data?.data!!
                    Log.d("testt", uri.toString())
                    showImagePreview()
                }
            }
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentUserInfoBinding.bind(view)

        (activity as ControlActivityInterface).viewNavigationBar()
        setClickListener()
        loadProfileImage()
    }

    private fun setClickListener() {
        listOf(
            binding.textViewUserInfoContent1,
            binding.textViewUserInfoContent2,
            binding.textViewUserInfoContent3,
            binding.textViewUserInfoContent4,
            binding.textViewUserInfoContent5,
            binding.textViewUserInfoContent6
        ).forEach { textView ->
            textView.setOnClickListener { handleOnClick(textView.id) }
        }
    }

    private fun handleOnClick(viewId: Int) {
        when (viewId) {
            R.id.textViewUserInfoContent1 -> requestPermission()//TODO 권한 체크로 변경하고 권한 체크하는 곳에서 이동하도록 변경
            R.id.textViewUserInfoContent2 -> navigateFitOff()
            R.id.textViewUserInfoContent3 -> announcement()
            R.id.textViewUserInfoContent4 -> navigateLicense()
            R.id.textViewUserInfoContent5 -> logout()
            R.id.textViewUserInfoContent6 -> withdraw()
        }
    }

    private fun navigateTo(actionId: Int) {
        findNavController().navigate(actionId)
    }

    private fun showImagePreview() {
        val imagePreviewView = layoutInflater.inflate(R.layout.dialog_user_thumbnail, null)
        val preview = imagePreviewView.findViewById<ImageView>(R.id.imageViewPreview)

        Glide.with(this).load(uri).into(preview)
        AlertDialog.Builder(requireContext()).setView(imagePreviewView)
            .setPositiveButton(android.R.string.ok) { dialog, _ ->
                dialog.dismiss()
                Glide.with(this).load(uri).apply(RequestOptions().circleCrop())
                    .into(binding.imageViewUserInfoIcon)
                imageUpload("test_user_id")
            }.setNegativeButton(android.R.string.cancel) { dialog, _ ->
                dialog.dismiss()
            }.show()
    }

    private fun imageUpload(userId: String) {
        val activity = activity ?: return
        val storage = Firebase.storage
        val storageRef = storage.getReference("user_profile")

        val fileName = "${userId}_profile.png"
        val userRef = storageRef.child(fileName)

        userRef.putFile(uri).addOnSuccessListener {
            userRef.downloadUrl.addOnSuccessListener { downloadUri ->
                val downloadURL = downloadUri.toString()
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
        registerForActivityResult.launch(
            Intent(
                Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI
            )
        )
    }

    private fun announcement() {
        Toast.makeText(context, "지정된 공지사항이 없습니다.", Toast.LENGTH_SHORT).show()
    }

    private fun logout() {
        navigateTo(R.id.action_userInfoFragment_to_loginFragment)
    }

    private fun withdraw() {
        navigateTo(R.id.action_userInfoFragment_to_loginFragment)
    }

    private fun navigateLicense() {
        navigateTo(R.id.action_userInfoFragment_to_licenseFragment)
    }

    private fun navigateFitOff() {
        navigateTo(R.id.action_userInfoFragment_to_myFitOffFragment)
    }

    //교육용 팝업 띄우는 메서드
    fun showPermissionDialog() {
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

    //권한 설정 화면을 위한 다이얼로그 띄우는 메서드
    fun showPermissionSettiongDialog() {
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
    fun navigateToAppSetting() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
            data = Uri.fromParts("package", requireContext().packageName, null)
        }
        startActivity(intent)
    }
    //권한 확인 및 요청 메서드
    fun requestPermission(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE){
            Log.d("myPermission","UPSIDE_DOWN_CAKE")
            multiplePermissionsLauncher.launch(arrayOf(Manifest.permission.READ_MEDIA_IMAGES,Manifest.permission.READ_MEDIA_VISUAL_USER_SELECTED))
        }else if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
            Log.d("myPermission","TIRAMISU")
            multiplePermissionsLauncher.launch(arrayOf(Manifest.permission.READ_MEDIA_IMAGES))
        }else{
            Log.d("myPermission","그외")
            multiplePermissionsLauncher.launch(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE))
        }
    }
}
