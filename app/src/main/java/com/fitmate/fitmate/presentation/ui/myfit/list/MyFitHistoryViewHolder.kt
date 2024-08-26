package com.fitmate.fitmate.ui.myfit.list

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.fitmate.fitmate.databinding.DialogFitHistoryDetailBinding
import com.fitmate.fitmate.databinding.ItemForListViewBinding
import com.fitmate.fitmate.databinding.ItemMyFitHistoryBinding
import com.fitmate.fitmate.domain.model.MyFitRecordHistoryDetail
import com.fitmate.fitmate.presentation.ui.dialog.fit_history.FitHistoryDialog
import com.fitmate.fitmate.presentation.ui.myfit.MyFitFragment

class MyFitHistoryViewHolder(
    private val binding: ItemMyFitHistoryBinding,
    private val fragment: MyFitFragment,
) : RecyclerView.ViewHolder(binding.root) {

    fun binding(item: MyFitRecordHistoryDetail) {
        binding.data = item

        binding.root.setOnClickListener {
            showDialog(item)
        }
    }
    private fun showDialog(item: MyFitRecordHistoryDetail){
        val customView = DialogFitHistoryDetailBinding.inflate(LayoutInflater.from(fragment.context))
        val dialog = FitHistoryDialog(customView, item)
        val fragmentManager = fragment.activity?.supportFragmentManager
        dialog.show(fragmentManager!!, "VoteDialog")
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