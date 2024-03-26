package com.fitmate.fitmate.presentation.ui.home

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.fitmate.fitmate.R
import com.fitmate.fitmate.databinding.FragmentGroupJoinBinding
import com.fitmate.fitmate.util.ControlActivityInterface

class GroupJoinFragment: Fragment(R.layout.fragment_group_join) {

    private lateinit var binding: FragmentGroupJoinBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentGroupJoinBinding.bind(view)

        (activity as ControlActivityInterface).goneNavigationBar()

        binding.toolbarGroupJoin.setNavigationOnClickListener { findNavController().navigate(R.id.action_groupJoinFragment_to_homeFragment) }
        binding.buttonJoinGroupConfirm.setOnClickListener { findNavController().navigate(R.id.action_groupJoinFragment_to_homeFragment) }
    }
}