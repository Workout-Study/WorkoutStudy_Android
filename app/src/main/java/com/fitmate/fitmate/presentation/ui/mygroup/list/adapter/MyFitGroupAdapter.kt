package com.fitmate.fitmate.presentation.ui.mygroup.list.adapter

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.fitmate.fitmate.ChatActivity
import com.fitmate.fitmate.databinding.ItemGroupBinding
import com.fitmate.fitmate.domain.model.FitGroup
import com.fitmate.fitmate.ui.myfit.list.MyFitGroupViewHolder

class MyFitGroupAdapter(private val itemClick:(FitGroup) -> Unit) : ListAdapter<FitGroup, MyFitGroupViewHolder>(
    diffUtil
) {


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): MyFitGroupViewHolder {
        return MyFitGroupViewHolder(ItemGroupBinding.inflate(LayoutInflater.from(parent.context),parent,false),itemClick)
    }

    override fun onBindViewHolder(holder: MyFitGroupViewHolder, position: Int) {
        holder.binding(currentList[position])
    }

    companion object {
        private val diffUtil = object: DiffUtil.ItemCallback<FitGroup>() {
            override fun areItemsTheSame(
                oldItem: FitGroup,
                newItem: FitGroup,
            ): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: FitGroup,
                newItem: FitGroup,
            ): Boolean {
                return oldItem.groupName == newItem.groupName
            }

        }
    }

}