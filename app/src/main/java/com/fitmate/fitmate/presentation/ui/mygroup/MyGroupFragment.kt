package com.fitmate.fitmate.presentation.ui.mygroup

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.fitmate.fitmate.R
import com.fitmate.fitmate.databinding.FragmentMyGroupBinding

class MyGroupFragment: Fragment(R.layout.fragment_my_group) {

    private lateinit var binding: FragmentMyGroupBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentMyGroupBinding.bind(view)

    }
}