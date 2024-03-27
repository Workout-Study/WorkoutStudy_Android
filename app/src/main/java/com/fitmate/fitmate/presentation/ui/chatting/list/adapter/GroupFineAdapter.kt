package com.fitmate.fitmate.presentation.ui.chatting.list.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.fitmate.fitmate.databinding.ItemGroupBinding
import com.fitmate.fitmate.domain.model.FitGroup
import com.fitmate.fitmate.presentation.ui.chatting.list.GroupFineViewHolder

class GroupFineAdapter(private val onClick: (FitGroup) -> Unit): ListAdapter<FitGroup, GroupFineViewHolder>(GroupFineDiffCallback) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroupFineViewHolder {
        val binding = ItemGroupBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return GroupFineViewHolder(binding, onClick)
    }

    override fun onBindViewHolder(holder: GroupFineViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    companion object {
        private val GroupFineDiffCallback = object: DiffUtil.ItemCallback<FitGroup>() {

            override fun areItemsTheSame(oldItem: FitGroup, newItem: FitGroup): Boolean {
                return oldItem.groupName == newItem.groupName
            }

            override fun areContentsTheSame(oldItem: FitGroup, newItem: FitGroup): Boolean {
                return oldItem == newItem
            }
        }
    }
}