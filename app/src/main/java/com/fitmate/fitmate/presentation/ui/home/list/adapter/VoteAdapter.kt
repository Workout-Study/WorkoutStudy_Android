package com.fitmate.fitmate.presentation.ui.home.list.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.fitmate.fitmate.databinding.ItemVoteBinding
import com.fitmate.fitmate.domain.model.VoteItem
import com.fitmate.fitmate.presentation.ui.home.list.VoteViewHolder

class VoteAdapter(private val fragment: Fragment, private val onClick: (VoteItem) -> Unit): ListAdapter<VoteItem, VoteViewHolder>(
    VoteItemDiffCallback
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VoteViewHolder {
        val binding = ItemVoteBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VoteViewHolder(binding, fragment, onClick)
    }

    override fun onBindViewHolder(holder: VoteViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    companion object {
        private val VoteItemDiffCallback = object: DiffUtil.ItemCallback<VoteItem>() {

            override fun areItemsTheSame(oldItem: VoteItem, newItem: VoteItem): Boolean {
                return oldItem.title == newItem.title
            }

            override fun areContentsTheSame(oldItem: VoteItem, newItem: VoteItem): Boolean {
                return oldItem == newItem
            }
        }
    }
}