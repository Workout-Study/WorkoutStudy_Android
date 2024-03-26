package com.fitmate.fitmate.presentation.ui.home.list

import androidx.recyclerview.widget.RecyclerView
import com.fitmate.fitmate.databinding.ItemCategoryBinding
import com.fitmate.fitmate.domain.model.CategoryItem
import com.fitmate.fitmate.domain.model.VoteItem

class CategoryViewHolder(private val binding: ItemCategoryBinding, val onClick: (CategoryItem) -> Unit) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(item: CategoryItem) {
        binding.categoryItem = item
        binding.root.setOnClickListener { onClick(item) }
    }
}