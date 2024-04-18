package com.fitmate.fitmate.presentation.ui.mygroup.list.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.fitmate.fitmate.databinding.ItemCertificateBinding
import com.fitmate.fitmate.databinding.ItemMakeGroupImageBinding
import com.fitmate.fitmate.domain.model.CertificationImage
import com.fitmate.fitmate.presentation.ui.certificate.list.CertificationImageViewHolder
import com.fitmate.fitmate.presentation.ui.mygroup.list.MakeGroupImageViewHolder

class MakeGroupImageAdapter(private val removeClick:(index:Int) -> Unit) : ListAdapter<CertificationImage, MakeGroupImageViewHolder>(
    diffUtil
) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): MakeGroupImageViewHolder {
        return MakeGroupImageViewHolder(ItemMakeGroupImageBinding.inflate(LayoutInflater.from(parent.context), parent, false),removeClick)
    }

    override fun onBindViewHolder(holder: MakeGroupImageViewHolder, position: Int) {
        holder.binding(currentList[position])
    }

    companion object {
        private val diffUtil = object: DiffUtil.ItemCallback<CertificationImage>() {
            override fun areItemsTheSame(
                oldItem: CertificationImage,
                newItem: CertificationImage,
            ): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: CertificationImage,
                newItem: CertificationImage,
            ): Boolean {
                return oldItem.imagesUri == newItem.imagesUri
            }

        }
    }

}