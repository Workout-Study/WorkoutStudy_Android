package com.fitmate.fitmate.presentation.ui.userinfo

import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.fitmate.fitmate.R
import com.fitmate.fitmate.databinding.FragmentUserInfoBinding
import com.fitmate.fitmate.util.ControlActivityInterface
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage

class UserInfoFragment: Fragment(R.layout.fragment_user_info) {

    private lateinit var binding: FragmentUserInfoBinding
    private lateinit var uri: Uri

    private val registerForActivityResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        when (result.resultCode) {
            AppCompatActivity.RESULT_OK -> {
                uri = result.data?.data!!
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
            binding.textViewUserInfoContent1, binding.textViewUserInfoContent2,
            binding.textViewUserInfoContent3, binding.textViewUserInfoContent4,
            binding.textViewUserInfoContent5, binding.textViewUserInfoContent6
        ).forEach { textView ->
            textView.setOnClickListener { handleOnClick(textView.id) }
        }
    }

    private fun handleOnClick(viewId: Int) {
        when (viewId) {
            R.id.textViewUserInfoContent1 -> {
                val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
                registerForActivityResult.launch(intent)
            }
            R.id.textViewUserInfoContent2 -> navigateTo(R.id.action_userInfoFragment_to_myFitOffFragment)
            R.id.textViewUserInfoContent3 -> Toast.makeText(context, "지정된 공지사항이 없습니다.", Toast.LENGTH_SHORT).show()
            R.id.textViewUserInfoContent4 -> navigateTo(R.id.action_userInfoFragment_to_licenseFragment)
            R.id.textViewUserInfoContent5 -> navigateTo(R.id.action_userInfoFragment_to_loginFragment)
            R.id.textViewUserInfoContent6 -> navigateTo(R.id.action_userInfoFragment_to_loginFragment)
        }
    }

    private fun navigateTo(actionId: Int) {
        findNavController().navigate(actionId)
    }

    private fun showImagePreview() {
        val imagePreviewView = layoutInflater.inflate(R.layout.dialog_user_thumbnail, null)
        val preview = imagePreviewView.findViewById<ImageView>(R.id.imageViewPreview)

        Glide.with(this).load(uri).into(preview)
        AlertDialog.Builder(requireContext())
            .setView(imagePreviewView)
            .setPositiveButton(android.R.string.ok) { dialog, _ ->
                dialog.dismiss()
                Glide.with(this)
                    .load(uri)
                    .apply(RequestOptions().circleCrop())
                    .into(binding.imageViewUserInfoIcon)
                imageUpload("test_user_id")
            }
            .setNegativeButton(android.R.string.cancel) { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    private fun imageUpload(userId: String) {
        val activity = activity ?: return
        val storage = Firebase.storage
        val storageRef = storage.getReference("user_profile")

        val fileName = "${userId}_profile.png"
        val userRef = storageRef.child(fileName)

        userRef.putFile(uri).addOnSuccessListener {
            activity.getSharedPreferences("UserInfo", AppCompatActivity.MODE_PRIVATE).edit().apply {
                putString("profileImageUri", uri.toString())
                apply()
            }
            Toast.makeText(activity, "성공적으로 프로필 사진을 변경하였습니다.", Toast.LENGTH_SHORT).show()
        }.addOnFailureListener {
            Toast.makeText(activity, "사진을 업로드하는데 실패하였습니다.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun loadProfileImage() {
        val sharedPreferences = requireActivity().getSharedPreferences("UserInfo", AppCompatActivity.MODE_PRIVATE)
        val profileImageUri = sharedPreferences.getString("profileImageUri", null)
        profileImageUri?.let {
            Glide.with(this)
                .load(Uri.parse(it))
                .apply(RequestOptions().circleCrop())
                .into(binding.imageViewUserInfoIcon)
        }
    }
}
