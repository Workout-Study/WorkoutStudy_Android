package com.fitmate.fitmate.presentation.ui.chatting.list.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.fitmate.fitmate.databinding.ItemProgressBinding
import com.fitmate.fitmate.domain.model.FitProgressItem

class GroupProgressAdapter (private val onClick: (FitProgressItem) -> Unit):
    ListAdapter<FitProgressItem, GroupProgressViewHolder>(GroupProgressDiffCallback) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroupProgressViewHolder {
        val binding = ItemProgressBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return GroupProgressViewHolder(binding, onClick)
    }

    override fun onBindViewHolder(holder: GroupProgressViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    companion object {
        private val GroupProgressDiffCallback = object: DiffUtil.ItemCallback<FitProgressItem>() {

            override fun areItemsTheSame(oldItem: FitProgressItem, newItem: FitProgressItem): Boolean {
                return oldItem.itemId == newItem.itemId
            }

            override fun areContentsTheSame(oldItem: FitProgressItem, newItem: FitProgressItem): Boolean {
                return oldItem == newItem
            }
        }
    }
}