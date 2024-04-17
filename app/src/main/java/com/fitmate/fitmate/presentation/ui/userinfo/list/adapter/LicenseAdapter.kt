package com.fitmate.fitmate.presentation.ui.userinfo.list.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.fitmate.fitmate.databinding.ItemLicenseBinding
import com.fitmate.fitmate.domain.model.LicenseItem
import com.fitmate.fitmate.presentation.ui.userinfo.list.LicenseViewHolder

class LicenseAdapter(private val fragment: Fragment): ListAdapter<LicenseItem, LicenseViewHolder>(LicenseProgressDiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LicenseViewHolder {
        val binding = ItemLicenseBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return LicenseViewHolder(fragment, binding)
    }

    override fun onBindViewHolder(holder: LicenseViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }
    companion object {
        private val LicenseProgressDiffCallback = object: DiffUtil.ItemCallback<LicenseItem>() {

            override fun areItemsTheSame(oldItem: LicenseItem, newItem: LicenseItem): Boolean {
                return oldItem.license == newItem.license
            }

            override fun areContentsTheSame(oldItem: LicenseItem, newItem: LicenseItem): Boolean {
                return oldItem == newItem
            }
        }
    }
}