package com.fitmate.fitmate.presentation.ui.chatting.list

import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import com.fitmate.fitmate.R
import com.fitmate.fitmate.databinding.DialogGroupVoteChangeToAgreeBinding
import com.fitmate.fitmate.databinding.ItemOppositeVoteBinding
import com.fitmate.fitmate.domain.model.GroupVoteCertificationDetail
import com.fitmate.fitmate.presentation.ui.chatting.dialog.VoteDialog
import com.fitmate.fitmate.presentation.ui.chatting.dialog.VoteFragmentInterface
import com.fitmate.fitmate.presentation.viewmodel.VoteViewModel

class GroupOppositeVoteViewHolder(
    private val binding: ItemOppositeVoteBinding,
    private val fragment: Fragment,
    private val voteFragmentInterface: VoteFragmentInterface,
    private val viewModel: VoteViewModel,
): VoteBindingViewHolder<ItemOppositeVoteBinding>(binding) {

    override fun bind(item: GroupVoteCertificationDetail) {
        binding.viewHolder = this
        binding.data = item
        binding.viewModel = viewModel

        try {
            val agreePercent = ((item.agreeCount / (item.agreeCount + item.disagreeCount.toFloat())) * 100).toInt()
            binding.textViewVoteAgreePercent.text = fragment.context?.getString(R.string.vote_agree_percent, agreePercent)
        }catch (e:ArithmeticException){
            binding.textViewVoteAgreePercent.text = fragment.context?.getString(R.string.vote_agree_percent,0)
        }
        try {
            val progressPercent = (((item.agreeCount + item.disagreeCount) / item.maxAgreeCount.toFloat()) * 100).toInt()
            binding.textViewVoteProgressPercent.text = fragment.context?.getString(R.string.vote_agree_percent, progressPercent)
        }catch (e:ArithmeticException){
            binding.textViewVoteProgressPercent.text = fragment.context?.getString(R.string.vote_agree_percent,0)
        }
    }

    fun showVoteDialog(item: GroupVoteCertificationDetail) {
        val customView = DialogGroupVoteChangeToAgreeBinding.inflate(LayoutInflater.from(fragment.requireContext()))
        val dialog = VoteDialog(voteFragmentInterface,customView,item)
        val fragmentManager = fragment.activity?.supportFragmentManager
        dialog.show(fragmentManager!!,"VoteDialog")
    }
}
