package com.fitmate.fitmate.presentation.ui.mygroup.list

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.fitmate.fitmate.databinding.ItemCertificateBinding
import com.fitmate.fitmate.databinding.ItemMakeGroupImageBinding
import com.fitmate.fitmate.domain.model.CertificationImage

class MakeGroupImageViewHolder(private val binding:ItemMakeGroupImageBinding, private val clickRemove:(index:Int)->Unit): RecyclerView.ViewHolder(binding.root) {

    fun binding(item:CertificationImage){
        binding.data = item
        binding.imageViewRemoveItemMakeGroupImage.setOnClickListener {
            clickRemove(adapterPosition)
        }

    }

}