package com.fitmate.fitmate.presentation.ui.myfit.list.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.fitmate.fitmate.R
import com.fitmate.fitmate.presentation.ui.myfit.list.DayViewHolder
import java.time.LocalDate

class DayListAdapter(
    //해당 월
    private val tempMonth: Int,
    //해당 월의 리스트
    private val dayList: MutableList<LocalDate>,
    private val onItemClick: (position: Int) -> Unit
) : ListAdapter<LocalDate, DayViewHolder>(
    object : DiffUtil.ItemCallback<LocalDate>() {
        override fun areItemsTheSame(oldItem: LocalDate, newItem: LocalDate): Boolean =
            oldItem == newItem

        override fun areContentsTheSame(oldItem: LocalDate, newItem: LocalDate): Boolean =
            oldItem == newItem
    }
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DayViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_list_day, parent, false)
        return DayViewHolder(view, tempMonth) { position ->
            onItemClick(position)
        }
    }

    override fun onBindViewHolder(holder: DayViewHolder, position: Int) {
        val date = dayList[position]
        holder.bind(date)
    }

    override fun getItemCount(): Int = dayList.size
}
