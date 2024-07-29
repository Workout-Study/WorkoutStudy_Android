package com.fitmate.fitmate.presentation.ui.chatting

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.fitmate.fitmate.ChatActivity
import com.fitmate.fitmate.R
import com.fitmate.fitmate.data.model.dto.GroupVoteCertificationDetailDto
import com.fitmate.fitmate.databinding.FragmentGroupVoteBinding
import com.fitmate.fitmate.presentation.ui.chatting.list.adapter.GroupVoteAdapter
import com.fitmate.fitmate.presentation.viewmodel.VoteViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

@AndroidEntryPoint
class GroupVoteFragment : Fragment(R.layout.fragment_group_vote) {

    private lateinit var binding: FragmentGroupVoteBinding
    private val viewModel: VoteViewModel by viewModels()
    private lateinit var voteListAdapter: GroupVoteAdapter
    private var groupId = -1
    private var userId: Int = -1
    private var accessToken: String = ""
    private var platform: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        groupId = arguments?.getInt("groupId") ?: -1
        if (groupId != -1) {
            Log.d("woojugoing_group_id", groupId.toString())
        } else {
            Log.d("woojugoing_group_id", groupId.toString())
            Toast.makeText(context, "Error: Group not found!", Toast.LENGTH_SHORT).show()
        }
        loadUserPreference()
        viewModel.fetchFitGroupVotes(groupId, userId)
    }

    private fun loadUserPreference() {
        val userPreference = (activity as ChatActivity).loadUserPreference()
        userId = userPreference.getOrNull(2)?.toString()?.toInt() ?: -1
        accessToken = userPreference.getOrNull(0)?.toString() ?: ""
        platform = userPreference.getOrNull(3)?.toString() ?: ""
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentGroupVoteBinding.bind(view)
        binding.materialToolbarGroupVote.setupWithNavController(findNavController())


        voteListAdapter = GroupVoteAdapter(this, viewModel) { }
        binding.recyclerGroupVote.apply {
            adapter = voteListAdapter
        }




        viewModel.fitGroupVotes.observe(viewLifecycleOwner) { eachFitResponse ->
            stopShimmer()
            val voteItems = eachFitResponse.fitCertificationDetails
            voteListAdapter.submitList(voteItems)
        }
    }

    private fun startShimmer() {
        binding.groupVoteShimmer.startShimmer()
        binding.groupVoteShimmer.visibility = View.VISIBLE
        binding.recyclerGroupVote.visibility = View.GONE
    }

    private fun stopShimmer() {
        binding.groupVoteShimmer.stopShimmer()
        binding.groupVoteShimmer.visibility = View.GONE
        binding.recyclerGroupVote.visibility = View.VISIBLE
    }

    private fun formatPercent(detail: GroupVoteCertificationDetailDto): Int {
        return if (detail.agreeCount + detail.disagreeCount > 0) (detail.agreeCount * 100) / (detail.agreeCount + detail.disagreeCount) else 0
    }


    private fun formatDate(voteEndDate: String): String {
        val endDate = ZonedDateTime.parse(voteEndDate)
        return endDate.format(DateTimeFormatter.ofPattern("(MM/dd) HH:mm 종료"))
    }
}
