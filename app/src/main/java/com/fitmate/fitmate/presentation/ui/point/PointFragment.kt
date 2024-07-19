package com.fitmate.fitmate.presentation.ui.point

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.fitmate.fitmate.R
import com.fitmate.fitmate.databinding.FragmentPointBinding

class PointFragment : Fragment() {
    private lateinit var binding:FragmentPointBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentPointBinding.inflate(layoutInflater)
        return binding.root
    }

}