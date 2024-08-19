package com.fitmate.fitmate.presentation.ui.login

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.fitmate.fitmate.MainActivity
import com.fitmate.fitmate.R
import com.fitmate.fitmate.databinding.FragmentNicknameBinding
import com.fitmate.fitmate.presentation.viewmodel.LoginViewModel
import com.fitmate.fitmate.util.ControlActivityInterface
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NicknameFragment: Fragment(R.layout.fragment_nickname) {

    lateinit var binding: FragmentNicknameBinding
    private val viewModel: LoginViewModel by viewModels()
    private val TAG = "NicknameFragment"
    private var userId: Int = -1
    private var accessToken: String = ""
    private var refreshToken: String = ""
    private var platform: String = ""
    private var createdAt: String = ""
    private val defaultProfile = "https://firebasestorage.googleapis.com/v0/b/fitmate-e2b03.appspot.com/o/user_profile%2Fdefault_profile.png?alt=media&token=a4b124d6-0ba1-4585-a259-61d13c608b07"


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentNicknameBinding.bind(view)
        (activity as? ControlActivityInterface)?.goneNavigationBar()
        loadUserPreference()

        binding.buttonNicknameConfirm.setOnClickListener {
            // TODO 닉네임 유효성 검사
            // TODO 유효성 검사 성공 시 서버에 닉네임 데이터 전송
            // TODO 전송 성공 시 홈 화면으로 이동
            (activity as? ControlActivityInterface)?.hideKeyboard()
            val nickname = binding.editTextNickname.text.toString()
            val authorizationCode = arguments?.getString("authorizationCode")
            authorizationCode?.let{
                viewModel.updateUserInfo(it, nickname, defaultProfile)
            }
            findNavController().navigate(R.id.homeFragment)
            Toast.makeText(context, "${nickname}님 환영합니다!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun loadUserPreference() {
        val userPreference = (activity as MainActivity).loadUserPreference()
        accessToken = userPreference.getOrNull(0)?.toString() ?: ""
        refreshToken = userPreference.getOrNull(1)?.toString() ?: ""
        userId = userPreference.getOrNull(2)?.toString()?.toInt() ?: -1
        platform = userPreference.getOrNull(3)?.toString() ?: ""
        createdAt = userPreference.getOrNull(4)?.toString() ?: ""
        Log.d(TAG, "$userId, $platform")
        Log.d(TAG, accessToken)
    }
}