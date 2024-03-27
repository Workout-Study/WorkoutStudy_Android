package com.fitmate.fitmate.presentation.ui.chatting.list

import androidx.recyclerview.widget.RecyclerView
import com.fitmate.fitmate.databinding.ItemVoteBinding
import com.fitmate.fitmate.domain.model.VoteItem

class GroupVoteViewHolder(private val binding: ItemVoteBinding, val onClick: (VoteItem) -> Unit): RecyclerView.ViewHolder(binding.root){
    fun bind(item: VoteItem) {
        binding.voteItem = item
        binding.root.setOnClickListener { onClick(item) }
    }
}