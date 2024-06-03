package com.fitmate.fitmate.presentation.ui.category

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.fitmate.fitmate.MainActivity
import com.fitmate.fitmate.R
import com.fitmate.fitmate.databinding.FragmentCategorySelectBinding

class CategorySelectFragment: Fragment(R.layout.fragment_category_select){

    private lateinit var binding: FragmentCategorySelectBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as MainActivity).viewNavigationBar()

        initView(view)
        setColorFilter(Color.BLACK)
        setCategoryClickListeners()
    }

    private fun initView(view: View) {
        binding = FragmentCategorySelectBinding.bind(view)
    }

    private fun setColorFilter(color: Int) {
        val imageViews = binding.run {
            listOf(
                imageCategory1, imageCategory2, imageCategory3,
                imageCategory4, imageCategory5, imageCategory6,
                imageCategory7, imageCategory8, imageCategory9,
                imageCategory10, imageCategory11, imageCategory12,
            )
        }

        for(imageView in imageViews) {
            imageView.setColorFilter(color)
        }
    }

    private fun setCategoryClickListeners() {
        val frames = binding.run {
            listOf(
                frameCategorySelect1, frameCategorySelect2, frameCategorySelect3,
                frameCategorySelect4, frameCategorySelect5, frameCategorySelect6,
                frameCategorySelect7, frameCategorySelect8, frameCategorySelect9,
                frameCategorySelect10, frameCategorySelect11, frameCategorySelect12,
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