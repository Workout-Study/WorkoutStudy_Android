package com.fitmate.fitmate.presentation.ui.point.list.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.fitmate.fitmate.data.model.dto.GetFitMateList
import com.fitmate.fitmate.data.model.dto.UserResponse
import com.fitmate.fitmate.databinding.ItemPointHistoryBinding
import com.fitmate.fitmate.domain.model.PointHistoryContent
import com.fitmate.fitmate.presentation.ui.point.list.PointHistoryViewHolder

class PointHistoryAdapter(private var fitMateData: GetFitMateList? = null, private var userImage: UserResponse? = null): PagingDataAdapter<PointHistoryContent, PointHistoryViewHolder>(DiffCallback) {
    override fun onBindViewHolder(holder: PointHistoryViewHolder, position: Int) {
        val item = getItem(position)
        if (item != null) holder.bind(item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PointHistoryViewHolder {
        val binding = ItemPointHistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PointHistoryViewHolder(binding, fitMateData, userImage)
    }

    companion object {
        private val DiffCallback = object: DiffUtil.ItemCallback<PointHistoryContent>() {
            override fun areItemsTheSame(oldItem: PointHistoryContent, newItem: PointHistoryContent): Boolean {
                return oldItem.depositUserId == newItem.depositUserId
            }

            override fun areContentsTheSame(oldItem: PointHistoryContent, newItem: PointHistoryContent): Boolean {
                return oldItem == newItem
            }
        }
    }
}