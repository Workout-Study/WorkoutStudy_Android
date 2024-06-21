package com.fitmate.fitmate.ui.myfit.list

import android.app.ActionBar.LayoutParams
import android.content.Context
import android.graphics.Color
import android.text.PrecomputedText.Params
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.fitmate.fitmate.R
import com.fitmate.fitmate.databinding.ItemForListViewBinding
import com.fitmate.fitmate.databinding.ItemMyFitHistoryBinding
import com.fitmate.fitmate.databinding.ItemProgressBinding
import com.fitmate.fitmate.domain.model.FitHistory
import com.fitmate.fitmate.domain.model.MyFitGroupProgress
import com.fitmate.fitmate.domain.model.MyFitRecordHistoryDetail
import com.fitmate.fitmate.domain.model.MyFitRecordHistoryGroupInfo

class MyFitHistoryViewHolder(
    private val binding: ItemMyFitHistoryBinding,
    private val context: Context,
) : RecyclerView.ViewHolder(binding.root) {

    fun binding(item: MyFitRecordHistoryDetail) {
        binding.data = item
        //내부 뷰
        binding.recyclerViewMyFitItemInnerForGroupName.adapter = FitHistoryGroupNameInnerAdapter(context)
        val groupNameList = item.registeredFitCertifications.map {
            it.fitGroupName
        }
        (binding.recyclerViewMyFitItemInnerForGroupName.adapter as FitHistoryGroupNameInnerAdapter).submitList(groupNameList)
    }
}

class FitHistoryGroupNameInnerAdapter(private val context:Context): ListAdapter<String, FitHistoryGroupNameInnerViewHolder>(diffUtil) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): FitHistoryGroupNameInnerViewHolder {
        return FitHistoryGroupNameInnerViewHolder(ItemForListViewBinding.inflate(LayoutInflater.from(context), parent, false))
    }

    override fun onBindViewHolder(holder: FitHistoryGroupNameInnerViewHolder, position: Int) {
        holder.binding(currentList[position])
    }

    companion object {
        private val diffUtil = object: DiffUtil.ItemCallback<String>() {
            override fun areItemsTheSame(
                oldItem: String,
                newItem: String,
            ): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: String,
                newItem: String,
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}

class FitHistoryGroupNameInnerViewHolder(
    private val binding: ItemForListViewBinding,
) : RecyclerView.ViewHolder(binding.root) {

    fun binding(item: String) {
        binding.data = item
    }
}