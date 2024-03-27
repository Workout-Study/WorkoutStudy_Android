package com.fitmate.fitmate.presentation.ui.chatting.list

import androidx.recyclerview.widget.RecyclerView
import com.fitmate.fitmate.databinding.ItemFitOffBinding
import com.fitmate.fitmate.domain.model.MyFitOff

class GroupFitOffViewHolder(private val binding: ItemFitOffBinding, val onClick: (MyFitOff) -> Unit): RecyclerView.ViewHolder(binding.root){
    fun bind(item: MyFitOff) {
        binding.data = item
        binding.root.setOnClickListener { onClick(item) }
    }
}