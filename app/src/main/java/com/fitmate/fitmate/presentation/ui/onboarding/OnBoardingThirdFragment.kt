package com.fitmate.fitmate.presentation.ui.onboarding

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.fitmate.fitmate.R
import com.fitmate.fitmate.databinding.FragmentOnboardingThirdBinding
import com.fitmate.fitmate.presentation.viewmodel.OnBoardingViewModel
import dagger.hilt.android.AndroidEntryPoint


class OnBoardingThirdFragment: Fragment() {
    private lateinit var binding: FragmentOnboardingThirdBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentOnboardingThirdBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.buttonNextFragment.setOnClickListener {
            val viewPager = activity?.findViewById<ViewPager2>(R.id.viewPagerOnboardingContainer)
            viewPager?.currentItem = 3
        }

    }

}