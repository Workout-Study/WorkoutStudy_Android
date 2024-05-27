package com.fitmate.fitmate.ui.myfit.list

import androidx.recyclerview.widget.RecyclerView
import com.fitmate.fitmate.databinding.ItemGroupBinding
import com.fitmate.fitmate.domain.model.FitGroup

class MyFitGroupViewHolder(private val binding:ItemGroupBinding, private val onClick: (FitGroup) -> Unit ): RecyclerView.ViewHolder(binding.root) {

    fun binding(item: FitGroup){
        binding.data = item

        binding.root.setOnClickListener {
            onClick(item)
        }
    }
}