package com.fitmate.fitmate.presentation.ui.chatting.list

import androidx.recyclerview.widget.RecyclerView
import com.fitmate.fitmate.databinding.ItemProgressBinding
import com.fitmate.fitmate.domain.model.FitProgressItem

class GroupProgressViewHolder (private val binding: ItemProgressBinding, val onClick: (FitProgressItem) -> Unit): RecyclerView.ViewHolder(binding.root){
    fun bind(item: FitProgressItem) {
        binding.data = item
        binding.root.setOnClickListener { onClick(item) }
    }
}