package com.fitmate.fitmate.ui.myfit.list

import androidx.recyclerview.widget.RecyclerView
import com.fitmate.fitmate.databinding.ItemProgressBinding
import com.fitmate.fitmate.domain.model.MyFitGroupProgress

class MyFitGroupProgressViewHolder(private val binding:ItemProgressBinding): RecyclerView.ViewHolder(binding.root) {

    fun binding(item:MyFitGroupProgress){
        binding.data = item
    }
}