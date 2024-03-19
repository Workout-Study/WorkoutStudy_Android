package com.fitmate.fitmate.ui.onboarding


import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.fitmate.fitmate.R
import com.fitmate.fitmate.databinding.FragmentOnboardingContainerBinding

class OnboardingContainerFragment : Fragment(R.layout.fragment_onboarding_container) {
    private lateinit var binding: FragmentOnboardingContainerBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentOnboardingContainerBinding.inflate(layoutInflater)

        //TODO ViewPager2 어댑터 생성
        //TODO ViewPager2 설정
        //TODO 온보딩 화면 프래그먼트 클래스 생성후 연결
        //TODO 온보딩 화면 프래그먼트 클래스 생성후 연결
        //TODO 온보딩 화면 설정
    }
}