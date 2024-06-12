package com.fitmate.fitmate.presentation.ui.onboarding

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.fitmate.fitmate.R
import com.fitmate.fitmate.databinding.FragmentOnBoardingFifthBinding

class OnBoardingFifthFragment : Fragment() {
    private lateinit var binding: FragmentOnBoardingFifthBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentOnBoardingFifthBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.buttonNextFragment.setOnClickListener {
            findNavController().navigate(R.id.action_onboardingContainerFragment_to_onBoardingPermissionFragment)
        }
    }
}