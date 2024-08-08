package com.fitmate.fitmate.ui.myfit.list

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.fitmate.fitmate.databinding.ItemForListViewBinding
import com.fitmate.fitmate.databinding.ItemMyFitHistoryBinding
import com.fitmate.fitmate.domain.model.MyFitRecordHistoryDetail

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