package com.fitmate.fitmate.presentation.ui.userinfo

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.request.RequestOptions
import com.fitmate.fitmate.R
import com.fitmate.fitmate.databinding.FragmentUserInfoBinding
import com.fitmate.fitmate.util.ControlActivityInterface
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import java.time.Instant

class UserInfoFragment: Fragment(R.layout.fragment_user_info) {

    private lateinit var binding: FragmentUserInfoBinding
    private lateinit var uri: Uri

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentUserInfoBinding.bind(view)

        (activity as ControlActivityInterface).viewNavigationBar()
        setClickListener()

        binding.textViewUserInfoContent1.setOnClickListener {
            var intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
            registerForActivityResult.launch(intent)
        }

        binding.imageViewUserInfoIcon.setOnClickListener {
            val storage = Firebase.storage
            val storageRef = storage.getReference("image")
            val fileName = "${Instant.now()}test"
            val mountainsRef = storageRef.child("${fileName}.png")

            val uploadTask = mountainsRef.putFile(uri)
            uploadTask.addOnSuccessListener { taskSnapshot ->
                Toast.makeText(activity, "업로드 성공", Toast.LENGTH_SHORT).show()
            }.addOnFailureListener {
                Toast.makeText(activity, "업로드 실패", Toast.LENGTH_SHORT).show()
            }

        }
    }

    private fun setClickListener() { listOf(
            binding.textViewUserInfoContent2,
            binding.textViewUserInfoContent3, binding.textViewUserInfoContent4,
            binding.textViewUserInfoContent5, binding.textViewUserInfoContent6
        ).forEach { textView -> textView.setOnClickListener { handleOnClick(textView.id) } }
    }

    private fun handleOnClick(viewId: Int) {
        when (viewId) {
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

    private val registerForActivityResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            when (result.resultCode) {
                AppCompatActivity.RESULT_OK -> {
                    uri = result.data?.data!!
                    binding.imageViewUserInfoIcon.setImageURI(uri)
                    Glide.with(this)
                        .load(uri) // 로드할 이미지의 URI
                        .apply(RequestOptions().transform(CircleCrop())) // 동그랗게 만들기
                        .into(binding.imageViewUserInfoIcon) // 이미지뷰에 적용
                }
            }
        }

}