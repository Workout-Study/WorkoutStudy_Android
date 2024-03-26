package com.fitmate.fitmate.presentation.ui.home.list

import androidx.recyclerview.widget.RecyclerView
import com.fitmate.fitmate.databinding.ItemVoteBinding
import com.fitmate.fitmate.domain.model.VoteItem

class VoteViewHolder(private val binding: ItemVoteBinding, val onClick: (VoteItem) -> Unit) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(item: VoteItem) {
        binding.voteItem = item
        binding.root.setOnClickListener { onClick(item) }
    }
}
