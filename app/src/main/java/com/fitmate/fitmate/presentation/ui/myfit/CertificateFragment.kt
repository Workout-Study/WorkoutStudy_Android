package com.fitmate.fitmate.ui.myfit

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.fitmate.fitmate.MainActivity
import com.fitmate.fitmate.databinding.FragmentCertificateBinding
import com.fitmate.fitmate.util.ControlActivityInterface

class CertificateFragment: Fragment() {
    private lateinit var binding: FragmentCertificateBinding
    private lateinit var controlActivityInterface: ControlActivityInterface
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCertificateBinding.inflate(layoutInflater)
        controlActivityInterface = activity as MainActivity
        controlActivityInterface.goneNavigationBar()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.materialToolbarCertificate.setupWithNavController(findNavController())


    }
}