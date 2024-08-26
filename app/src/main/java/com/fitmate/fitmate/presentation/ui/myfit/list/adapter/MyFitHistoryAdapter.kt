package com.fitmate.fitmate.ui.myfit.list.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.fitmate.fitmate.databinding.ItemMyFitHistoryBinding
import com.fitmate.fitmate.domain.model.MyFitRecordHistoryDetail
import com.fitmate.fitmate.presentation.ui.myfit.MyFitFragment
import com.fitmate.fitmate.ui.myfit.list.MyFitHistoryViewHolder

class MyFitHistoryAdapter(private val fragment: MyFitFragment): ListAdapter<MyFitRecordHistoryDetail, MyFitHistoryViewHolder>(diffUtil) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): MyFitHistoryViewHolder {
        return MyFitHistoryViewHolder(ItemMyFitHistoryBinding.inflate(LayoutInflater.from(fragment.context), parent, false), fragment)
    }

    override fun onBindViewHolder(holder: MyFitHistoryViewHolder, position: Int) {
        holder.binding(currentList[position])
    }

    companion object {
        private val diffUtil = object: DiffUtil.ItemCallback<MyFitRecordHistoryDetail>() {
            override fun areItemsTheSame(
                oldItem: MyFitRecordHistoryDetail,
                newItem: MyFitRecordHistoryDetail,
            ): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: MyFitRecordHistoryDetail,
                newItem: MyFitRecordHistoryDetail,
            ): Boolean {
                return oldItem.fitRecordId == newItem.fitRecordId
            }
        }
    }
}