package com.fitmate.fitmate.presentation.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.fitmate.fitmate.R
import com.fitmate.fitmate.databinding.FragmentHomeCategoryBinding
import com.fitmate.fitmate.domain.model.CategoryItem
import com.fitmate.fitmate.presentation.ui.home.list.adapter.CategoryAdapter

class HomeCategoryFragment: Fragment(R.layout.fragment_home_category) {

    private lateinit var binding: FragmentHomeCategoryBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeCategoryBinding.inflate(inflater, container, false)
        setFitGroup()
        return binding.root
    }

    private fun setFitGroup() {
        val recyclerView: RecyclerView = binding.recyclerViewCategory
        val adapter = CategoryAdapter { _ -> findNavController().navigate(R.id.action_homeFragment_to_groupJoinFragment) }
        val testItems = listOf(CategoryItem(title = "Test FitGroup", fitCount = "5회 / 1주", peopleCount = "12 / 20", comment = "Test FitGroup 입니다."))

        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = adapter
        adapter.submitList(testItems)
    }
}