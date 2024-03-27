package com.fitmate.fitmate.presentation.ui.onboarding

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.fitmate.fitmate.R
import com.fitmate.fitmate.databinding.FragmentOnboardingThirdBinding

class OnBoardingThirdFragment: Fragment(R.layout.fragment_onboarding_third) {
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
        initViews()
    }

    private fun initViews() {
        binding.buttonFinishFragment.setOnClickListener {
            findNavController().navigate(R.id.action_onboardingContainerFragment_to_onBoardingPermissionFragment)
        }
    }

}