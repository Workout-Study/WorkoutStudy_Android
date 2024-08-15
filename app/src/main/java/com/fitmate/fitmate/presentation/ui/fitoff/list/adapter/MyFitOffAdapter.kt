package com.fitmate.fitmate.presentation.ui.fitoff.list.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.fitmate.fitmate.databinding.ItemFitOffBinding
import com.fitmate.fitmate.domain.model.FitOff
import com.fitmate.fitmate.presentation.ui.fitoff.ViewFitOffFragment
import com.fitmate.fitmate.presentation.ui.fitoff.list.MyFitOffViewHolder

class MyFitOffAdapter(private val fragment:ViewFitOffFragment, private val fitOffOwnerNameInfo:Any) : ListAdapter<FitOff, MyFitOffViewHolder>(diffUtil) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): MyFitOffViewHolder {
        val binding = ItemFitOffBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return MyFitOffViewHolder(binding, fragment, fitOffOwnerNameInfo)
    }

    override fun onBindViewHolder(holder: MyFitOffViewHolder, position: Int) {
        holder.binding(currentList[position])
    }

    companion object {
        private val diffUtil = object: DiffUtil.ItemCallback<FitOff>() {
            override fun areItemsTheSame(
                oldItem: FitOff,
                newItem: FitOff,
            ): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: FitOff,
                newItem: FitOff,
            ): Boolean {
                return oldItem.fitOffId == newItem.fitOffId
            }
        }
    }
}