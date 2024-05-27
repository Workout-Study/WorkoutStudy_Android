package com.fitmate.fitmate.presentation.ui.chatting

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.fitmate.fitmate.R
import com.fitmate.fitmate.databinding.FragmentGroupFineBinding
import com.fitmate.fitmate.domain.model.FitGroup
import com.fitmate.fitmate.presentation.ui.chatting.list.adapter.GroupFineAdapter

class GroupFineFragment: Fragment(R.layout.fragment_group_fine) {

    private lateinit var binding: FragmentGroupFineBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentGroupFineBinding.bind(view)

        binding.materialToolbarGroupFine.setupWithNavController(findNavController())

        val recyclerView: RecyclerView = binding.recyclerFine
        val adapter = GroupFineAdapter {}
        val testItems = listOf(FitGroup("김성호", "03/13", "5,000", 1, 1))

        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = adapter
        adapter.submitList(testItems)

    }
}