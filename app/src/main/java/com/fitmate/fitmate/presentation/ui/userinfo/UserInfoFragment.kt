package com.fitmate.fitmate.presentation.ui.userinfo

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.fitmate.fitmate.MainActivity
import com.fitmate.fitmate.R
import com.fitmate.fitmate.databinding.FragmentUserInfoBinding
import com.fitmate.fitmate.presentation.viewmodel.LoginViewModel
import com.fitmate.fitmate.util.ControlActivityInterface
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UserInfoFragment : Fragment(R.layout.fragment_user_info) {

    private lateinit var binding: FragmentUserInfoBinding
    private val viewModel: LoginViewModel by viewModels()
    private val TAG = "UserInfoFragment"
    private var userId: Int = -1
    private var accessToken: String = ""
    private var platform: String = ""

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentUserInfoBinding.bind(view)

        (activity as ControlActivityInterface).viewNavigationBar()
        loadUserPreference()
        setClickListener()
        viewModel.getUserInfo(userId)
        observeViewModel()
    }

    private fun observeViewModel() {
        viewModel.userInfo.observe(viewLifecycleOwner) { user ->
            user?.let {
                binding.textViewUserInfoName.text = it.nickname
                binding.textViewUserInfoDate.text = it.createdAt
                if(it.imageUrl != null) {
                    Glide.with(binding.imageViewUserInfoIcon.context)
                        .load(it.imageUrl)
                        .transform(CenterCrop(), RoundedCorners(16))
                        .error(R.drawable.ic_launcher_logo)
                        .into(binding.imageViewUserInfoIcon)
                } else {
                    Log.d("woojugoing", "image is null")
                }
            }
        }
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
            binding.textViewUserInfoFitOff,
            binding.textViewUserInfoPoint,
            binding.textViewUserInfoNotice,
            binding.textViewUserInfoFAQ,
            binding.textViewUserInfoOSS,
            binding.textViewUserInfoLogout,
            binding.textViewUserInfoWithDraw
        ).forEach { textView ->
            textView.setOnClickListener { handleOnClick(textView.id) }
        }
    }

    private fun handleOnClick(viewId: Int) {
        when (viewId) {
            R.id.textViewUserInfoProfile -> navigateProfile() // TODO 권한 체크로 변경하고 권한 체크하는 곳에서 이동하도록 변경
            R.id.textViewUserInfoFitOff -> navigateFitOff()
            R.id.textViewUserInfoPoint -> navigatePoint()
            R.id.textViewUserInfoNotice -> notice()
            R.id.textViewUserInfoFAQ -> null
            R.id.textViewUserInfoOSS -> navigateLicense()
            R.id.textViewUserInfoLogout -> logout()
            R.id.textViewUserInfoWithDraw -> withdraw()
        }
    }

    private fun navigateTo(actionId: Int) { findNavController().navigate(actionId) }

    private fun navigateProfile() {
        navigateTo(R.id.action_userInfoFragment_to_profileFragment)
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
                Snackbar.make(binding.root, "로그아웃을 성공했습니다. [USERID ${userId}]", Snackbar.LENGTH_SHORT).show()
            }
        }
    }

    private fun withdraw() {
        viewModel.deleteUser(userId)
        Snackbar.make(binding.root, "회원틸퇴를 성공했습니다. [USERID ${userId}]", Snackbar.LENGTH_SHORT).show()
    }

    private fun navigateLicense() {
        navigateTo(R.id.action_userInfoFragment_to_licenseFragment)
    }

    private fun navigateFitOff() {
        Toast.makeText(context, "추후 업데이트 예정입니다.", Toast.LENGTH_SHORT).show()
    }

    private fun navigatePoint() {
        Toast.makeText(context, "추후 업데이트 예정입니다.", Toast.LENGTH_SHORT).show()
        navigateTo(R.id.action_userInfoFragment_to_loginFragment)
    }

}
