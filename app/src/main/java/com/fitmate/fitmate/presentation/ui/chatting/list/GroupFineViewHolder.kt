package com.fitmate.fitmate.presentation.ui.chatting.list

import androidx.recyclerview.widget.RecyclerView
import com.fitmate.fitmate.databinding.ItemGroupBinding
import com.fitmate.fitmate.domain.model.FitGroup

class GroupFineViewHolder(private val binding: ItemGroupBinding, val onClick: (FitGroup) -> Unit): RecyclerView.ViewHolder(binding.root) {
    fun bind(item: FitGroup) {
        binding.data = item
        binding.root.setOnClickListener { onClick(item) }
    }
}
