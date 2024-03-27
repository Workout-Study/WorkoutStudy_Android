package com.fitmate.fitmate.presentation.ui.chatting

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.fitmate.fitmate.R
import com.fitmate.fitmate.databinding.FragmentGroupVoteBinding
import com.fitmate.fitmate.domain.model.VoteItem
import com.fitmate.fitmate.presentation.ui.chatting.list.adapter.GroupVoteAdapter

class GroupVoteFragment: Fragment(R.layout.fragment_group_vote) {

    private lateinit var binding: FragmentGroupVoteBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentGroupVoteBinding.bind(view)

        binding.materialToolbarGroupVote.setupWithNavController(findNavController())

        val recyclerView: RecyclerView = binding.recyclerGroupVote
        val adapter = GroupVoteAdapter {}

        recyclerView.layoutManager = LinearLayoutManager(context)
        val testItems = listOf(VoteItem(title = "Test FitGroup", fitmate = "김성호", percent = 3, time = 5))
        recyclerView.adapter = adapter
        adapter.submitList(testItems)
    }
}