package com.fitmate.fitmate.ui.myfit.list

import androidx.recyclerview.widget.RecyclerView
import com.fitmate.fitmate.databinding.ItemFitOffBinding
import com.fitmate.fitmate.domain.model.MyFitOff

class MyFitOffViewHolder(private val binding:ItemFitOffBinding): RecyclerView.ViewHolder(binding.root) {

    fun binding(item: MyFitOff){
        binding.data = item
    }
}