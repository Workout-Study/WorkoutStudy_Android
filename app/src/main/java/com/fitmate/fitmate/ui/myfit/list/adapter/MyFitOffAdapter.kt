package com.fitmate.fitmate.ui.myfit.list.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.fitmate.fitmate.databinding.ItemFitOffBinding
import com.fitmate.fitmate.domain.model.MyFitOff
import com.fitmate.fitmate.ui.myfit.list.MyFitOffViewHolder

class MyFitOffAdapter : ListAdapter<MyFitOff, MyFitOffViewHolder>(diffUtil) {


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): MyFitOffViewHolder {
        return MyFitOffViewHolder(ItemFitOffBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: MyFitOffViewHolder, position: Int) {
        holder.binding(currentList[position])
    }

    companion object {
        private val diffUtil = object: DiffUtil.ItemCallback<MyFitOff>() {
            override fun areItemsTheSame(
                oldItem: MyFitOff,
                newItem: MyFitOff,
            ): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: MyFitOff,
                newItem: MyFitOff,
            ): Boolean {
                return oldItem.title == newItem.title
            }

        }
    }

}