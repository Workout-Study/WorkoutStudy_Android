package com.fitmate.fitmate.presentation.ui.chatting

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.fitmate.fitmate.R
import com.fitmate.fitmate.databinding.FragmentGroupFitOffBinding
import com.fitmate.fitmate.domain.model.MyFitOff
import com.fitmate.fitmate.presentation.ui.chatting.list.adapter.GroupFitOffAdapter

class GroupFitOffFragment: Fragment(R.layout.fragment_group_fit_off) {

    private lateinit var binding: FragmentGroupFitOffBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentGroupFitOffBinding.bind(view)
        binding.materialToolbarGroupFitOff.setupWithNavController(findNavController())

        val recyclerView: RecyclerView = binding.recyclerViewGroupFitOff
        val adapter = GroupFitOffAdapter {}
        val testItems = listOf(MyFitOff(title = "김성호", reason = "그냥"))

        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = adapter
        adapter.submitList(testItems)
    }

}