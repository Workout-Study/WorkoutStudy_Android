package com.fitmate.fitmate.presentation.ui.userinfo

import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.Manifest.permission.READ_MEDIA_IMAGES
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.TextView
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
import androidx.navigation.ui.setupWithNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.fitmate.fitmate.MainActivity
import com.fitmate.fitmate.R
import com.fitmate.fitmate.databinding.FragmentProfileBinding
import com.fitmate.fitmate.presentation.viewmodel.LoginViewModel
import com.fitmate.fitmate.util.ControlActivityInterface
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileFragment : Fragment(R.layout.fragment_profile) {

    private lateinit var binding: FragmentProfileBinding
    private val viewModel: LoginViewModel by viewModels()
    private var userId: Int = -1
    private var imageUri: Uri? = null

    private val multiplePermissionsLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
        permissions.entries.forEach { (permission, isGranted) ->
            handlePermissionResult(permission, isGranted)
        }
    }

    private val pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        if (uri != null) showImagePreview(uri)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentProfileBinding.bind(view)
        (activity as ControlActivityInterface).goneNavigationBar()
        loadUserPreference()
        loadProfileImage()
        setListener()
    }

    private fun setListener() {
        binding.imageViewProfliePhoto.setOnClickListener { requestPermission() }
        binding.buttonProfileConfirm.setOnClickListener { sendServerChangeProfile() }
        binding.toolbarProfile.setupWithNavController(findNavController())
        binding.editTextProfileName.setOnEditorActionListener(TextView.OnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE || event?.keyCode == KeyEvent.KEYCODE_ENTER) {
                val imm = activity?.getSystemService(AppCompatActivity.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(v.windowToken, 0)
                return@OnEditorActionListener true
            }
            false
        })
    }

    private fun loadUserPreference() {
        val userPreference = (activity as MainActivity).loadUserPreference()
        userId = userPreference.getOrNull(2)?.toString()?.toInt() ?: -1
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

    private fun showImagePreview(uri: Uri) {
        imageUri = uri
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
                    .into(binding.imageViewProfliePhoto)
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
        val sharedPreferences = requireActivity().getSharedPreferences("UserInfo", AppCompatActivity.MODE_PRIVATE)
        val profileImageUri = sharedPreferences.getString("profileImageUri", null)
        profileImageUri?.let {
            Glide.with(this).load(Uri.parse(it)).apply(RequestOptions().circleCrop())
                .into(binding.imageViewProfliePhoto)
        }
    }

    private fun accessGallery() {
        pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private fun showPermissionDialog() {
        MaterialAlertDialogBuilder(requireContext(), R.style.Theme_Fitmate_Dialog)
            .setTitle(getString(R.string.permission_dialog_scr_guide))
            .setMessage(getString(R.string.permission_dialog_scr_guide_message))
            .setPositiveButton(getString(R.string.permission_dialog_scr_guide_select)) { _, _ ->
                requestPermission()
            }
            .setNegativeButton(getString(R.string.permission_dialog_scr_guide_cancel)) { dialogInterface: DialogInterface, _ ->
                dialogInterface.cancel()
            }.show()
    }

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

    private fun navigateToAppSetting() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
            data = Uri.fromParts("package", requireContext().packageName, null)
        }
        startActivity(intent)
    }

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
            multiplePermissionsLauncher.launch(arrayOf(READ_MEDIA_IMAGES))
        }  else if (ContextCompat.checkSelfPermission(requireContext(), READ_EXTERNAL_STORAGE) == PERMISSION_GRANTED) {
            accessGallery()
        } else {
            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.S_V2){
                multiplePermissionsLauncher.launch(arrayOf(READ_EXTERNAL_STORAGE))
            }else{
                multiplePermissionsLauncher.launch(arrayOf(READ_MEDIA_IMAGES))
            }
        }
    }

    private fun sendServerChangeProfile() {
        imageUri?.let { imageUpload(userId, it) }
        if(binding.editTextProfileName.length() != 0) {
            val newNickName = binding.editTextProfileName.text.toString()
            viewModel.updateNickname(userId, newNickName)
        }

        findNavController().navigate(R.id.userInfoFragment)
    }
}
