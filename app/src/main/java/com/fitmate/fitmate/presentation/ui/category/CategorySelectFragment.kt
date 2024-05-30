package com.fitmate.fitmate.presentation.ui.category

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.fitmate.fitmate.MainActivity
import com.fitmate.fitmate.R
import com.fitmate.fitmate.databinding.FragmentCategorySelectBinding

class CategorySelectFragment: Fragment(R.layout.fragment_category_select){

    lateinit var binding: FragmentCategorySelectBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as MainActivity).viewNavigationBar()

        initView(view)

        binding.buttoncategoryselect.setOnClickListener {
            findNavController().navigate(R.id.action_categorySelectFragment_to_categoryFragment)
        }
    }

    private fun initView(view:View) {
        binding = FragmentCategorySelectBinding.bind(view)
    }
}