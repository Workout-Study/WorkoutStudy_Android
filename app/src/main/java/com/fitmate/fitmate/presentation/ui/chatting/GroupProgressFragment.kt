package com.fitmate.fitmate.presentation.ui.chatting

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.fitmate.fitmate.R
import com.fitmate.fitmate.databinding.FragmentProgressBinding
import com.fitmate.fitmate.domain.model.MyFitGroupProgress
import com.fitmate.fitmate.presentation.ui.chatting.list.adapter.GroupProgressAdapter

class GroupProgressFragment: Fragment(R.layout.fragment_progress) {

    private lateinit var binding: FragmentProgressBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentProgressBinding.bind(view)

        binding.materialToolbarProgress.setupWithNavController(findNavController())

        val recyclerView: RecyclerView = binding.recyclerProgress
        val adapter = GroupProgressAdapter {}
        val testItems = listOf(MyFitGroupProgress("김성호", null, 7, 3))

        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = adapter
        adapter.submitList(testItems)
    }
}