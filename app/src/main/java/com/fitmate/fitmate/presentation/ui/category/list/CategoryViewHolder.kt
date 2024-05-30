package com.fitmate.fitmate.presentation.ui.category.list

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
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
            fragment.findNavController().navigate(R.id.action_homeMainFragment_to_groupJoinFragment, bundle)
        }

        Glide.with(binding.imageViewItemCategoryFitgroupThumbnail.context)
            .load(item.thumbnail)
            .transform(CenterCrop(), RoundedCorners(16))
            .error(R.drawable.ic_launcher_logo)
            .into(binding.imageViewItemCategoryFitgroupThumbnail)
    }
}