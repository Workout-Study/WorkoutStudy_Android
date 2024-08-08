package com.fitmate.fitmate.presentation.ui.chatting.list.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.fitmate.fitmate.databinding.ItemGroupProgressBinding
import com.fitmate.fitmate.databinding.ItemProgressBinding
import com.fitmate.fitmate.domain.model.FitProgressItem
import com.fitmate.fitmate.domain.model.ItemFitMateProgressForAdapter

class GroupProgressAdapter (private val onClick: (ItemFitMateProgressForAdapter) -> Unit):
    ListAdapter<ItemFitMateProgressForAdapter, GroupProgressViewHolder>(GroupProgressDiffCallback) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroupProgressViewHolder {
        val binding = ItemGroupProgressBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return GroupProgressViewHolder(binding, onClick)
    }

    override fun onBindViewHolder(holder: GroupProgressViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    companion object {
        private val GroupProgressDiffCallback = object: DiffUtil.ItemCallback<ItemFitMateProgressForAdapter>() {

            override fun areItemsTheSame(oldItem: ItemFitMateProgressForAdapter, newItem: ItemFitMateProgressForAdapter): Boolean {
                return oldItem.fitMateUserId == newItem.fitMateUserId
            }

            override fun areContentsTheSame(oldItem: ItemFitMateProgressForAdapter, newItem: ItemFitMateProgressForAdapter): Boolean {
                return oldItem == newItem
            }
        }
    }
}