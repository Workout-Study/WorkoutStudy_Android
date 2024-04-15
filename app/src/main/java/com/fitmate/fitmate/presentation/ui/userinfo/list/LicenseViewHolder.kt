package com.fitmate.fitmate.presentation.ui.userinfo.list

import android.content.Intent
import android.graphics.Paint
import android.net.Uri
import androidx.recyclerview.widget.RecyclerView
import com.fitmate.fitmate.databinding.ItemLicenseBinding
import com.fitmate.fitmate.domain.model.LicenseItem

class LicenseViewHolder(private val binding: ItemLicenseBinding): RecyclerView.ViewHolder(binding.root) {

    fun bind(item: LicenseItem) {
        binding.data = item
        binding.textViewLicenseItemLink.paintFlags = Paint.UNDERLINE_TEXT_FLAG
        binding.textViewLicenseItemLink.setOnClickListener {
            itemView.context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(item.url)))
        }
    }
}