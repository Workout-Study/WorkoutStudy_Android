package com.fitmate.fitmate.presentation.ui.certificate.list

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.fitmate.fitmate.databinding.ItemCertificateBinding
import com.fitmate.fitmate.domain.model.CertificationImage

class CertificationImageViewHolder(private val binding:ItemCertificateBinding, private val clickRemove:(index:Int)->Unit): RecyclerView.ViewHolder(binding.root) {

    fun binding(item:CertificationImage,isVisible:Boolean){
        binding.data = item
        if (isVisible){
            binding.imageViewRemoveItemCertificationImage.visibility = View.VISIBLE
        }else{
            binding.imageViewRemoveItemCertificationImage.visibility = View.GONE
        }
        binding.imageViewRemoveItemCertificationImage.setOnClickListener {
            clickRemove(adapterPosition)
        }

    }

}