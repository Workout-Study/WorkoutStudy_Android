package com.fitmate.fitmate.presentation.ui.mygroup

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.fitmate.fitmate.MainActivity
import com.fitmate.fitmate.R
import com.fitmate.fitmate.databinding.FragmentMakeGroupBinding
import com.fitmate.fitmate.util.ControlActivityInterface

class MakeGroupFragment: Fragment(R.layout.fragment_make_group) {
    private lateinit var binding: FragmentMakeGroupBinding
    private lateinit var controlActivityInterface: ControlActivityInterface
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMakeGroupBinding.inflate(layoutInflater)
        controlActivityInterface = activity as MainActivity
        controlActivityInterface.goneNavigationBar()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.materialToolbar2.setupWithNavController(findNavController())
        //구현
    }

}