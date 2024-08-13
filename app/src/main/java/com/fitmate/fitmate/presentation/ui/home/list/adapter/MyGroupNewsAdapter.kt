package com.fitmate.fitmate.presentation.ui.home.list.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.fitmate.fitmate.databinding.ItemMyGroupNewsBinding
import com.fitmate.fitmate.databinding.ItemVoteBinding
import com.fitmate.fitmate.domain.model.MyGroupNews
import com.fitmate.fitmate.domain.model.VoteItem
import com.fitmate.fitmate.presentation.ui.home.list.VoteViewHolder
import com.fitmate.fitmate.presentation.viewmodel.HomeViewModel

class MyGroupNewsAdapter(private val fragment: Fragment, private val viewModel: HomeViewModel, val onClick: (MyGroupNews) -> Unit): PagingDataAdapter<MyGroupNews, VoteViewHolder>(
    VoteItemDiffCallback
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VoteViewHolder {
        val binding = ItemMyGroupNewsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VoteViewHolder(binding, fragment, viewModel, onClick)
    }

    override fun onBindViewHolder(holder: VoteViewHolder, position: Int) {
        val item = getItem(position)
        if (item != null) holder.bind(item)
    }

    companion object {
        private val VoteItemDiffCallback = object: DiffUtil.ItemCallback<MyGroupNews>() {

            override fun areItemsTheSame(oldItem: MyGroupNews, newItem: MyGroupNews): Boolean {
                return oldItem.issueDate == newItem.issueDate
            }

            override fun areContentsTheSame(oldItem: MyGroupNews, newItem: MyGroupNews): Boolean {
                return oldItem == newItem
            }
        }
    }
}