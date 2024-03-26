package com.fitmate.fitmate.ui.myfit.list.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.fitmate.fitmate.databinding.ItemMyFitHistoryBinding
import com.fitmate.fitmate.databinding.ItemProgressBinding
import com.fitmate.fitmate.domain.model.FitHistory
import com.fitmate.fitmate.domain.model.MyFitGroupProgress
import com.fitmate.fitmate.ui.myfit.list.MyFitGroupProgressViewHolder
import com.fitmate.fitmate.ui.myfit.list.MyFitHistoryViewHolder

class MyFitHistoryAdapter(private val context:Context): ListAdapter<FitHistory, MyFitHistoryViewHolder>(diffUtil) {


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): MyFitHistoryViewHolder {
        return MyFitHistoryViewHolder(ItemMyFitHistoryBinding.inflate(LayoutInflater.from(context), parent, false),context)
    }

    override fun onBindViewHolder(holder: MyFitHistoryViewHolder, position: Int) {
        Log.d("testt","리사이클러뷰 생성")
        holder.binding(currentList[position])
    }

    companion object {
        private val diffUtil = object: DiffUtil.ItemCallback<FitHistory>() {
            override fun areItemsTheSame(
                oldItem: FitHistory,
                newItem: FitHistory,
            ): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: FitHistory,
                newItem: FitHistory,
            ): Boolean {
                return oldItem == newItem
            }

        }
    }

}