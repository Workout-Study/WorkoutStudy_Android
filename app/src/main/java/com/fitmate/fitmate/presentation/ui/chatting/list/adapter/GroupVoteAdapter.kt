package com.fitmate.fitmate.presentation.ui.chatting.list.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.fitmate.fitmate.databinding.ItemVoteBinding
import com.fitmate.fitmate.domain.model.VoteItem
import com.fitmate.fitmate.domain.usecase.GroupUseCase
import com.fitmate.fitmate.presentation.ui.chatting.list.GroupVoteViewHolder
import com.fitmate.fitmate.presentation.viewmodel.GroupViewModel

class GroupVoteAdapter(private val fragment: Fragment, private val viewModel: GroupViewModel, private val onclick: (VoteItem) -> Unit):
    ListAdapter<VoteItem, GroupVoteViewHolder>(VoteItemDiffCallback) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroupVoteViewHolder {
        val binding = ItemVoteBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return GroupVoteViewHolder(binding, fragment, viewModel, onclick)
    }

    override fun onBindViewHolder(holder: GroupVoteViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    companion object {
        private val VoteItemDiffCallback = object: DiffUtil.ItemCallback<VoteItem>() {

            override fun areItemsTheSame(oldItem: VoteItem, newItem: VoteItem): Boolean {
                return oldItem.title == newItem.title
            }

            override fun areContentsTheSame(oldItem: VoteItem, newItem: VoteItem): Boolean {
                return oldItem == newItem
            }
        }
    }
}