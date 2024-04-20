package com.fitmate.fitmate.presentation.ui.myfit.list

import android.util.Log
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.fitmate.fitmate.R
import java.time.DayOfWeek
import java.time.LocalDate
import androidx.core.content.ContextCompat
import com.fitmate.fitmate.databinding.ItemListDayBinding
import com.fitmate.fitmate.domain.model.MyFitRecordHistoryDetail
import com.fitmate.fitmate.presentation.ui.myfit.MyFitFragment
import java.time.Instant
import java.time.ZoneId

class DayViewHolder(
    view: View,
    private val tempMonth: Int,
    private val calendarHandler: MyFitFragment.CalendarHandler,
    private val onItemClick: (position: Int) -> Unit
) : RecyclerView.ViewHolder(view) {
    private val binding = ItemListDayBinding.bind(view)

    fun bind(date: LocalDate) {
        val context = binding.root.context
        binding.itemDayText.text = date.dayOfMonth.toString()
        val dayOfWeekColor = when (date.dayOfWeek) {
            DayOfWeek.SATURDAY -> ContextCompat.getColor(context, R.color.stateBlue)
            DayOfWeek.SUNDAY -> ContextCompat.getColor(context, R.color.turquoise)
            else -> ContextCompat.getColor(context, R.color.black)
        }

        if (date.isEqual(LocalDate.now())) {
            binding.itemDayLayout.setBackgroundResource(R.drawable.bg_border_selected)
        } else {
            binding.itemDayText.setTextColor(dayOfWeekColor)
        }

        if (calendarHandler.fitRecordHistoryDataResult.isNotEmpty()){

            val fitDateList = calendarHandler.fitRecordHistoryDataResult.map { fitHistoryData ->
                    Instant.parse(fitHistoryData.recordStartDate).atZone(ZoneId.systemDefault()).toLocalDate()
                }
            if (fitDateList.contains(date)){
                binding.itemDayText.setTextColor(ContextCompat.getColor(context, R.color.black))
                binding.imageViewItemDayCurrentDate.setImageResource(R.drawable.ic_circle)
                binding.imageViewItemDayCurrentDate.visibility = View.VISIBLE
                binding.imageViewItemDayCurrentDate.setColorFilter(ContextCompat.getColor(context, R.color.turquoise))
                binding.imageViewItemDayCurrentDate.alpha = 0.3f
            }
        }

        binding.itemDayText.alpha = if (tempMonth != date.monthValue) 0.0f else 1.0f

        binding.root.setOnClickListener {
            if (tempMonth != date.monthValue){
                return@setOnClickListener
            }else{
                onItemClick(date.dayOfMonth)
            }

        }
    }
}
