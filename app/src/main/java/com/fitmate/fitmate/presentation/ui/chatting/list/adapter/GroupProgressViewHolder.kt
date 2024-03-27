package com.fitmate.fitmate.presentation.ui.chatting.list.adapter

import androidx.recyclerview.widget.RecyclerView
import com.fitmate.fitmate.databinding.ItemProgressBinding
import com.fitmate.fitmate.domain.model.MyFitGroupProgress

class GroupProgressViewHolder (private val binding: ItemProgressBinding, val onClick: (MyFitGroupProgress) -> Unit): RecyclerView.ViewHolder(binding.root){
    fun bind(item: MyFitGroupProgress) {
        binding.data = item
        binding.root.setOnClickListener { onClick(item) }
    }
}