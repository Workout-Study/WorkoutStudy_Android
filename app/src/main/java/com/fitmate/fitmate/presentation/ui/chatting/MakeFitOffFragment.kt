package com.fitmate.fitmate.presentation.ui.chatting

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.fitmate.fitmate.R
import com.fitmate.fitmate.databinding.FragmentMakeFitOffBinding

class MakeFitOffFragment: Fragment(R.layout.fragment_make_fit_off) {

    private lateinit var binding: FragmentMakeFitOffBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentMakeFitOffBinding.bind(view)
    }
}