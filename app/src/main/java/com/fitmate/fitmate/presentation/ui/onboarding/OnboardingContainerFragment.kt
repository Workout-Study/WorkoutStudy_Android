package com.fitmate.fitmate.presentation.ui.onboarding


import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.fitmate.fitmate.MainActivity
import com.fitmate.fitmate.R
import com.fitmate.fitmate.databinding.FragmentOnboardingContainerBinding
import com.fitmate.fitmate.presentation.ui.onboarding.adapter.ViewPagerAdapter
import com.fitmate.fitmate.util.ControlActivityInterface
import okhttp3.internal.ignoreIoExceptions

class OnboardingContainerFragment : Fragment() {
    private lateinit var binding: FragmentOnboardingContainerBinding
    private lateinit var controlActivityInterface: ControlActivityInterface
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentOnboardingContainerBinding.inflate(layoutInflater)
        controlActivityInterface = activity as MainActivity

        //바텀 네비게이션 gone
        controlActivityInterface.goneNavigationBar()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //뷰페이저 설정
        initViews()
    }

    private fun initViews() {
        val fragmentList = arrayListOf(
            OnBoardingFirstFragment(),
            OnBoardingSecondFragment(),
            OnBoardingThirdFragment(),
            OnBoardingFourthFragment(),
            OnBoardingFifthFragment(),
        )

        //뷰패이저 어댑터 설정
        val adapter = ViewPagerAdapter(
            fragmentList,
            requireActivity().supportFragmentManager,
            lifecycle
        )

        //뷰페이저 어댑터 및 인디케이터 연결
        binding.viewPagerOnboardingContainer.adapter = adapter
        binding.dotsIndicator.attachTo(binding.viewPagerOnboardingContainer)
        binding.viewPagerOnboardingContainer.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                if (position == 4){
                    binding.dotsIndicator.visibility = View.GONE
                }else{
                    binding.dotsIndicator.visibility = View.VISIBLE
                }
            }

            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels)
            }

            override fun onPageScrollStateChanged(state: Int) {
                super.onPageScrollStateChanged(state)
            }
        })
    }

}