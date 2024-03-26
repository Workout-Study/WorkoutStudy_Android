package com.fitmate.fitmate.presentation.ui.userinfo

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.fitmate.fitmate.R
import com.fitmate.fitmate.databinding.FragmentLicenseBinding
import com.fitmate.fitmate.util.ControlActivityInterface

class LicenseFragment: Fragment(R.layout.fragment_license) {

    private lateinit var binding: FragmentLicenseBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentLicenseBinding.bind(view)

        (activity as ControlActivityInterface).goneNavigationBar()
        binding.toolbarLicense.setupWithNavController(findNavController())
    }
}