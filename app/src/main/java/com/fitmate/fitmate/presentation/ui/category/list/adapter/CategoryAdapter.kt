package com.fitmate.fitmate.presentation.ui.category.list.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.fitmate.fitmate.databinding.ItemCategoryBinding
import com.fitmate.fitmate.domain.model.CategoryItem
import com.fitmate.fitmate.presentation.ui.category.list.CategoryViewHolder

class CategoryAdapter(private val fragment: Fragment, private val onClick: (CategoryItem) -> Unit): ListAdapter<CategoryItem, CategoryViewHolder>(
    CategoryItemDiffCallback
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val binding = ItemCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CategoryViewHolder(binding, fragment, onClick)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    companion object {
        private val CategoryItemDiffCallback = object: DiffUtil.ItemCallback<CategoryItem>() {

            override fun areItemsTheSame(oldItem: CategoryItem, newItem: CategoryItem): Boolean {
                return oldItem.title == newItem.title
            }

            override fun areContentsTheSame(oldItem: CategoryItem, newItem: CategoryItem): Boolean {
                return oldItem == newItem
            }
        }
    }
}
