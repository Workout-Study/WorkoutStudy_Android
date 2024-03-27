package com.fitmate.fitmate.presentation.ui.onboarding

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.fitmate.fitmate.R
import com.fitmate.fitmate.databinding.FragmentOnboardingPermissionBinding

class OnBoardingPermissionFragment: Fragment() {
    private lateinit var binding: FragmentOnboardingPermissionBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentOnboardingPermissionBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonFragmentOnboardingPermissionToLogin.setOnClickListener {
            //권한 물어보고 로그인 화면으로 이동
            //TODO 권한 리스트들 물어보기
            //TODO 온보딩 완료 값 로컬에 저장
            findNavController().navigate(R.id.action_onBoardingPermissionFragment_to_loginFragment)
        }
    }


    private fun onBoardingFinished() {
        val sharedPref = activity?.getSharedPreferences("onBoarding", Context.MODE_PRIVATE)
        val editor = sharedPref?.edit()
        editor?.putBoolean("Finished", true)
        editor?.apply()
    }
}

