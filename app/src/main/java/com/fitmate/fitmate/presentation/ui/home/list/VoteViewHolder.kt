package com.fitmate.fitmate.presentation.ui.home.list

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.fitmate.fitmate.R
import com.fitmate.fitmate.databinding.ItemVoteBinding
import com.fitmate.fitmate.domain.model.VoteItem

class VoteViewHolder(private val binding: ItemVoteBinding, private val fragment: Fragment, val onClick: (VoteItem) -> Unit) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(item: VoteItem) {
        binding.voteItem = item
        binding.buttonItemVoteFitgroupVote.visibility = View.GONE
        binding.root.setOnClickListener {
            onClick(item)
            val bundle = Bundle()
            bundle.putInt("groupId", item.groupId)
            fragment.findNavController().navigate(R.id.groupVoteFragment2, bundle)
        }

        Glide.with(binding.imageViewItemCategoryFitgroupThumbnail.context)
            .load(item.image)
            .transform(CenterCrop(), RoundedCorners(16))
            .into(binding.imageViewItemCategoryFitgroupThumbnail)
    }
}
