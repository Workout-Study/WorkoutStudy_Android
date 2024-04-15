package com.fitmate.fitmate.ui.myfit.list

import androidx.recyclerview.widget.RecyclerView
import com.fitmate.fitmate.databinding.ItemProgressBinding
import com.fitmate.fitmate.domain.model.FitProgressItem

class MyFitGroupProgressViewHolder(private val binding:ItemProgressBinding): RecyclerView.ViewHolder(binding.root) {

    fun binding(item: FitProgressItem){
        binding.data = item
    }
}