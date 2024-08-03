package com.fitmate.fitmate.presentation.ui.chatting.list

import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import com.fitmate.fitmate.databinding.DialogGroupVoteChangeToOppositeBinding
import com.fitmate.fitmate.databinding.ItemAgreeVoteBinding
import com.fitmate.fitmate.domain.model.GroupVoteCertificationDetail
import com.fitmate.fitmate.presentation.ui.chatting.dialog.VoteDialog
import com.fitmate.fitmate.presentation.ui.chatting.dialog.VoteFragmentInterface
import com.fitmate.fitmate.presentation.viewmodel.VoteViewModel
import java.time.Duration
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter

class GroupAgreeVoteViewHolder(
    private val binding: ItemAgreeVoteBinding,
    private val fragment: Fragment,
    private val voteFragmentInterface: VoteFragmentInterface,
    private val viewModel: VoteViewModel,
): VoteBindingViewHolder<ItemAgreeVoteBinding>(binding) {

    override fun bind(item: GroupVoteCertificationDetail) {
        binding.viewHolder = this
        binding.data = item
        binding.viewModel = viewModel
    }

    fun showVoteDialog(item: GroupVoteCertificationDetail) {
        val customView = DialogGroupVoteChangeToOppositeBinding.inflate(LayoutInflater.from(fragment.requireContext()))
        val dialog = VoteDialog(voteFragmentInterface,customView,item)
        val fragmentManager = fragment.activity?.supportFragmentManager
        dialog.show(fragmentManager!!,"VoteDialog")
    }
}
