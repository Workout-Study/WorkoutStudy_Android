package com.fitmate.fitmate.presentation.ui.home.list

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.fitmate.fitmate.R
import com.fitmate.fitmate.databinding.ItemCategoryBinding
import com.fitmate.fitmate.domain.model.CategoryItem

class CategoryViewHolder(private val binding: ItemCategoryBinding, private val fragment: Fragment, val onClick: (CategoryItem) -> Unit) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(item: CategoryItem) {
        binding.categoryItem = item
        binding.root.setOnClickListener {
            onClick(item)
            val bundle = Bundle()
            bundle.putInt("groupId", item.fitGroupId)
            fragment.findNavController().navigate(R.id.action_homeFragment_to_groupJoinFragment, bundle)
        }
    }
}