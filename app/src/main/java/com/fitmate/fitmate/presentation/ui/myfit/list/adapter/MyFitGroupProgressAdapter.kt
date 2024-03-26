package com.fitmate.fitmate.ui.myfit.list.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.fitmate.fitmate.databinding.ItemProgressBinding
import com.fitmate.fitmate.domain.model.MyFitGroupProgress
import com.fitmate.fitmate.ui.myfit.list.MyFitGroupProgressViewHolder

class MyFitGroupProgressAdapter : ListAdapter<MyFitGroupProgress, MyFitGroupProgressViewHolder>(diffUtil) {


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): MyFitGroupProgressViewHolder {
        return MyFitGroupProgressViewHolder(ItemProgressBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: MyFitGroupProgressViewHolder, position: Int) {
        holder.binding(currentList[position])
    }

    companion object {
        private val diffUtil = object: DiffUtil.ItemCallback<MyFitGroupProgress>() {
            override fun areItemsTheSame(
                oldItem: MyFitGroupProgress,
                newItem: MyFitGroupProgress,
            ): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: MyFitGroupProgress,
                newItem: MyFitGroupProgress,
            ): Boolean {
                return oldItem.title == newItem.title
            }

        }
    }

}