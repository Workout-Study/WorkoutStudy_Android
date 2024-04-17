package com.fitmate.fitmate.presentation.ui.userinfo.list

import android.content.Intent
import android.graphics.Paint
import android.net.Uri
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.fitmate.fitmate.R
import com.fitmate.fitmate.databinding.ItemLicenseBinding
import com.fitmate.fitmate.domain.model.LicenseItem
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class LicenseViewHolder(private val fragment: Fragment, private val binding: ItemLicenseBinding): RecyclerView.ViewHolder(binding.root) {

    fun bind(item: LicenseItem) {
        binding.data = item
        binding.textViewLicenseItemLink.paintFlags = Paint.UNDERLINE_TEXT_FLAG
        binding.textViewLicenseItemLink.setOnClickListener {
            itemView.context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(item.url)))
        }
        binding.textViewLicenseItemlicense.setOnClickListener {
            val licenseText = when(binding.textViewLicenseItemlicense.text) {
                "[ Apache License 2.0 ]" -> LicenseData.apacheLicenseText
                "[ MIT License ]" -> LicenseData.mitLicenseText
                "[ IconScout Simple License ]" -> LicenseData.iconScoutText
                "[ BSD License ]" -> LicenseData.bsdLicenseText
                "[ Eclipse Public License 1.0 ]" -> LicenseData.eclipseLicenseText
                else -> ""
            }
            MaterialAlertDialogBuilder(fragment.requireContext(), R.style.Theme_Fitmate_license)
                .setMessage(licenseText)
                .show()
        }
    }
}