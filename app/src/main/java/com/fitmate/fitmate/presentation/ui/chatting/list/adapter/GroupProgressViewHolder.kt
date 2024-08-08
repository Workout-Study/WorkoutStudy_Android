package com.fitmate.fitmate.presentation.ui.chatting.list.adapter

import androidx.recyclerview.widget.RecyclerView
import com.fitmate.fitmate.databinding.ItemGroupProgressBinding
import com.fitmate.fitmate.databinding.ItemProgressBinding
import com.fitmate.fitmate.domain.model.FitProgressItem
import com.fitmate.fitmate.domain.model.ItemFitMateProgressForAdapter

class GroupProgressViewHolder (private val binding: ItemGroupProgressBinding, val onClick: (ItemFitMateProgressForAdapter) -> Unit): RecyclerView.ViewHolder(binding.root){
    fun bind(item: ItemFitMateProgressForAdapter) {
        binding.data = item
        binding.root.setOnClickListener { onClick(item) }
    }
}