package com.fitmate.fitmate.presentation.ui.chatting.list.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.fitmate.fitmate.databinding.ItemVoteBinding
import com.fitmate.fitmate.domain.model.GroupVoteCertificationDetail
import com.fitmate.fitmate.domain.model.VoteItem
import com.fitmate.fitmate.domain.usecase.GroupUseCase
import com.fitmate.fitmate.presentation.ui.chatting.list.GroupVoteViewHolder
import com.fitmate.fitmate.presentation.viewmodel.GroupViewModel
import com.fitmate.fitmate.presentation.viewmodel.VoteViewModel

class GroupVoteAdapter(
    private val fragment: Fragment,
    private val viewModel: VoteViewModel,
    private val onclick: (VoteItem) -> Unit
) :
    ListAdapter<GroupVoteCertificationDetail, GroupVoteViewHolder>(VoteItemDiffCallback) {

    override fun getItemViewType(position: Int): Int {
        return super.getItemViewType(position)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroupVoteViewHolder {
        val binding = ItemVoteBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return GroupVoteViewHolder(binding, fragment, viewModel, onclick)
    }

    override fun onBindViewHolder(holder: GroupVoteViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    companion object {
        const val VIEW_TYPE_NEW_VOTE = true
        const val VIEW_TYPE_OLD_VOTE = false

        private val VoteItemDiffCallback =
            object : DiffUtil.ItemCallback<GroupVoteCertificationDetail>() {

                override fun areItemsTheSame(
                    oldItem: GroupVoteCertificationDetail,
                    newItem: GroupVoteCertificationDetail
                ): Boolean {
                    return oldItem.certificationId == newItem.certificationId
                }

                override fun areContentsTheSame(
                    oldItem: GroupVoteCertificationDetail,
                    newItem: GroupVoteCertificationDetail
                ): Boolean {
                    return oldItem == newItem
                }
            }
    }
}