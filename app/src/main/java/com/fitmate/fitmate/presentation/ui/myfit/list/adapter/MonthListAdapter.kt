package com.fitmate.fitmate.presentation.ui.myfit.list.adapter

import android.provider.ContactsContract.Data
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.fitmate.fitmate.databinding.ItemListMonthBinding
import com.fitmate.fitmate.domain.model.MyFitRecordHistoryDetail
import com.fitmate.fitmate.presentation.ui.myfit.MyFitFragment
import com.fitmate.fitmate.presentation.ui.myfit.list.MonthView
import com.fitmate.fitmate.presentation.ui.myfit.list.MonthViewHolder
import java.util.Calendar
import java.util.Date
import kotlin.properties.Delegates

class MonthListAdapter(
    val calendarHandler: MyFitFragment.CalendarHandler,
    private val onItemClick: (data: List<MyFitRecordHistoryDetail>) -> Unit
) : ListAdapter<Int, MonthViewHolder>(
    object : DiffUtil.ItemCallback<Int>() {
        override fun areItemsTheSame(oldItem: Int, newItem: Int): Boolean =
            oldItem == newItem


        override fun areContentsTheSame(oldItem: Int, newItem: Int): Boolean =
            oldItem == newItem
    }
) {
    val center = Int.MAX_VALUE / 2

    override fun getItemCount(): Int = Int.MAX_VALUE

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MonthViewHolder {
        val binding = ItemListMonthBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return MonthView(binding, calendarHandler, onItemClick)
    }

    override fun onBindViewHolder(holder: MonthViewHolder, position: Int) {
        val monthOffset = position - center
        holder.bind(monthOffset, position)
    }
}
