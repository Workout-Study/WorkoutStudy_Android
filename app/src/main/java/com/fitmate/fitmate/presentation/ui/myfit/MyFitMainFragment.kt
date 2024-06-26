package com.fitmate.fitmate.presentation.ui.myfit

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.fitmate.fitmate.MainActivity
import com.fitmate.fitmate.databinding.FragmentMyFitMainBinding
import com.fitmate.fitmate.databinding.FragmentMyfitBinding

class MyFitMainFragment: Fragment() {
    private lateinit var binding: FragmentMyFitMainBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentMyFitMainBinding.inflate(layoutInflater)
        return binding.root
    }
}