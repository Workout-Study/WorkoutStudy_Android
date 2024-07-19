package com.fitmate.fitmate.presentation.ui.point.list

import androidx.recyclerview.widget.RecyclerView
import com.fitmate.fitmate.databinding.ItemCategoryBinding
import com.fitmate.fitmate.databinding.ItemPointHistoryBinding
import com.fitmate.fitmate.domain.model.CategoryItem
import com.fitmate.fitmate.domain.model.PointHistoryContent

class PointHistoryViewHolder(private val binding: ItemPointHistoryBinding): RecyclerView.ViewHolder(binding.root) {
    fun bind(item: PointHistoryContent) {
        binding.data = item
    }
}