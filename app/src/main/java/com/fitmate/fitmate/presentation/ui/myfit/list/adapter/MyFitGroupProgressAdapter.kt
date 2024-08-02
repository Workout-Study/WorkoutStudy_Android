package com.fitmate.fitmate.ui.myfit.list.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.fitmate.fitmate.databinding.ItemProgressBinding
import com.fitmate.fitmate.domain.model.FitProgressItem
import com.fitmate.fitmate.ui.myfit.list.MyFitGroupProgressViewHolder

class MyFitGroupProgressAdapter(private val context: Context) : ListAdapter<FitProgressItem, MyFitGroupProgressViewHolder>(diffUtil) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): MyFitGroupProgressViewHolder {
        return MyFitGroupProgressViewHolder(ItemProgressBinding.inflate(LayoutInflater.from(parent.context), parent, false),context)
    }

    override fun onBindViewHolder(holder: MyFitGroupProgressViewHolder, position: Int) {
        holder.binding(currentList[position])
    }

    companion object {
        private val diffUtil = object: DiffUtil.ItemCallback<FitProgressItem>() {
            override fun areItemsTheSame(
                oldItem: FitProgressItem,
                newItem: FitProgressItem,
            ): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: FitProgressItem,
                newItem: FitProgressItem,
            ): Boolean {
                return oldItem.itemId == newItem.itemId
            }

        }
    }

}