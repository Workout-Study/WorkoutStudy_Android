package com.fitmate.fitmate.presentation.ui.category

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.fitmate.fitmate.MainActivity
import com.fitmate.fitmate.R
import com.fitmate.fitmate.databinding.FragmentCategorySelectBinding

class CategorySelectFragment: Fragment(R.layout.fragment_category_select) {

    private lateinit var binding: FragmentCategorySelectBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as MainActivity).viewNavigationBar()

        initView(view)
        setCategoryClickListeners()
    }

    private fun initView(view: View) {
        binding = FragmentCategorySelectBinding.bind(view)
    }


    private fun setCategoryClickListeners() {
        val frames = binding.run {
            listOf(
                frameCategorySelect1, frameCategorySelect2, frameCategorySelect3,
                frameCategorySelect4, frameCategorySelect5, frameCategorySelect6,
                frameCategorySelect7, frameCategorySelect8, frameCategorySelect9,
                frameCategorySelect10
            )
        }

        val clickListener = View.OnClickListener { view ->
            val id = view.id
            val frameName = resources.getResourceEntryName(id)
            val number = frameName.filter { it.isDigit() }.toIntOrNull()
            val bundle = Bundle().apply {
                putInt("categoryNumber", number ?: -1)
            }
            findNavController().navigate(R.id.categoryFragment, bundle)
        }

        for(frame in frames) frame.setOnClickListener(clickListener)
    }
}