package com.fitmate.fitmate.presentation.ui.onboarding

import android.Manifest
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.fitmate.fitmate.R
import com.fitmate.fitmate.databinding.FragmentOnboardingPermissionBinding
import com.fitmate.fitmate.presentation.viewmodel.OnBoardingViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OnBoardingPermissionFragment: Fragment() {
    val permissions =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
                arrayOf(Manifest.permission.POST_NOTIFICATIONS,Manifest.permission.READ_MEDIA_IMAGES)
        }else{
            arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
        }

    private lateinit var binding: FragmentOnboardingPermissionBinding
    private val viewModel: OnBoardingViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentOnboardingPermissionBinding.inflate(layoutInflater)
        binding.view = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.onboardingInquiryStatus.observe(viewLifecycleOwner){
            if (it){
                findNavController().navigate(R.id.action_onBoardingPermissionFragment_to_homeMainFragment)
            }
        }
    }

    fun setButtonClick() {
        multiplePermissionsLauncher.launch(permissions)
    }

    val multiplePermissionsLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
        permissions.entries.forEach { (permission, isGranted) -> }
        viewModel.saveOnBoardingStateInPref()
    }

}

