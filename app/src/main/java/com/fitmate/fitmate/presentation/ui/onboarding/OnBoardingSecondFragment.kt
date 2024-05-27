package com.fitmate.fitmate.presentation.ui.onboarding

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.fitmate.fitmate.R
import com.fitmate.fitmate.databinding.FragmentOnboardingSecondBinding

class OnBoardingSecondFragment: Fragment(R.layout.fragment_onboarding_second) {
    private lateinit var binding: FragmentOnboardingSecondBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentOnboardingSecondBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.buttonNextFragment.setOnClickListener {
            val viewPager = activity?.findViewById<ViewPager2>(R.id.viewPagerOnboardingContainer)
            viewPager?.currentItem = 2
        }
    }
}