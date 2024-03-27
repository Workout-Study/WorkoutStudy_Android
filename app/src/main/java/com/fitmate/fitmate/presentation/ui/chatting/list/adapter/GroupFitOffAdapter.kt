package com.fitmate.fitmate.presentation.ui.chatting.list.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.fitmate.fitmate.databinding.ItemFitOffBinding
import com.fitmate.fitmate.domain.model.MyFitOff
import com.fitmate.fitmate.presentation.ui.chatting.list.GroupFitOffViewHolder

class GroupFitOffAdapter(private val onClick: (MyFitOff) -> Unit):
    ListAdapter<MyFitOff, GroupFitOffViewHolder>(GroupFitOffDiffCallback) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroupFitOffViewHolder {
        val binding = ItemFitOffBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return GroupFitOffViewHolder(binding, onClick)
    }

    override fun onBindViewHolder(holder: GroupFitOffViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }
    
    companion object {
        private val GroupFitOffDiffCallback = object: DiffUtil.ItemCallback<MyFitOff>() {

            override fun areItemsTheSame(oldItem: MyFitOff, newItem: MyFitOff): Boolean {
                return oldItem.title == newItem.title
            }

            override fun areContentsTheSame(oldItem: MyFitOff, newItem: MyFitOff): Boolean {
                return oldItem == newItem
            }
        }
    }
}