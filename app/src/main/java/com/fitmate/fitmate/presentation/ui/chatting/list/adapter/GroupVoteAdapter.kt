package com.fitmate.fitmate.presentation.ui.chatting.list.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.fitmate.fitmate.databinding.ItemAgreeVoteBinding
import com.fitmate.fitmate.databinding.ItemOppositeVoteBinding
import com.fitmate.fitmate.databinding.ItemVoteBinding
import com.fitmate.fitmate.domain.model.GroupVoteCertificationDetail
import com.fitmate.fitmate.domain.model.VoteItem
import com.fitmate.fitmate.presentation.ui.chatting.list.GroupAgreeVoteViewHolder
import com.fitmate.fitmate.presentation.ui.chatting.list.GroupOppositeVoteViewHolder
import com.fitmate.fitmate.presentation.ui.chatting.list.GroupProcessingVoteViewHolder
import com.fitmate.fitmate.presentation.ui.chatting.list.VoteBindingViewHolder
import com.fitmate.fitmate.presentation.viewmodel.VoteViewModel

class GroupVoteAdapter(
    private val fragment: Fragment,
    private val viewModel: VoteViewModel,
    private val onclick: (VoteItem) -> Unit
) : ListAdapter<GroupVoteCertificationDetail, VoteBindingViewHolder<*>>(VoteItemDiffCallback) {

    override fun getItemViewType(position: Int): Int {
        return if (!currentList[position].isUserVoteDone){
            VIEW_TYPE_UNFINISHED_VOTE
        }else if(currentList[position].isUserVoteDone && currentList[position].isUserAgree){
            VIEW_TYPE_AGREE_VOTE
        }else{
            VIEW_TYPE_OPPOSITE_VOTE
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VoteBindingViewHolder<*> {
        return when(viewType){
            VIEW_TYPE_UNFINISHED_VOTE -> {
                val binding = ItemVoteBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                GroupProcessingVoteViewHolder(binding, fragment, viewModel, onclick)
            }
            VIEW_TYPE_AGREE_VOTE -> {
                val binding = ItemAgreeVoteBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                GroupAgreeVoteViewHolder(binding, fragment, viewModel, onclick)
            }
            VIEW_TYPE_OPPOSITE_VOTE -> {
                val binding = ItemOppositeVoteBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                GroupOppositeVoteViewHolder(binding, fragment, viewModel, onclick)
            }
            else -> {
                throw IllegalArgumentException("Invalid view type")
            }
        }

    }

    override fun onBindViewHolder(holder: VoteBindingViewHolder<*>, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    companion object {
        const val VIEW_TYPE_UNFINISHED_VOTE = 0
        const val VIEW_TYPE_AGREE_VOTE = 1
        const val VIEW_TYPE_OPPOSITE_VOTE = 2

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