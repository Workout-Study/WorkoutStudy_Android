package com.fitmate.fitmate.presentation.ui.certificate.list.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.fitmate.fitmate.databinding.ItemCertificateBinding
import com.fitmate.fitmate.domain.model.CertificationImage
import com.fitmate.fitmate.presentation.ui.certificate.list.CertificationImageViewHolder

class CertificationImageAdapter(private val removeClick:(index:Int) -> Unit) : ListAdapter<CertificationImage, CertificationImageViewHolder>(
    diffUtil
) {
    //삭제 버튼 visible 설정을 위한 변수
    private var isVisible = true

    //삭제 버튼 설정 버튼
    fun changeVisible(visible:Boolean) {
        isVisible = visible
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): CertificationImageViewHolder {
        return CertificationImageViewHolder(ItemCertificateBinding.inflate(LayoutInflater.from(parent.context), parent, false),removeClick)
    }

    override fun onBindViewHolder(holder: CertificationImageViewHolder, position: Int) {
        holder.binding(currentList[position],isVisible)
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