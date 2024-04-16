package com.fitmate.fitmate.presentation.ui.myfit.list

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.fitmate.fitmate.R
import java.time.DayOfWeek
import java.time.LocalDate
import androidx.core.content.ContextCompat
import com.fitmate.fitmate.databinding.ItemListDayBinding

class DayViewHolder(
    view: View,
    private val tempMonth: Int,
    private val onItemClick: (position: Int) -> Unit
) : RecyclerView.ViewHolder(view) {
    private val binding = ItemListDayBinding.bind(view)

    private val fitDates = listOf( // TODO 예시데이터를 집어넣은 것으로, 해당 달에 운동한 날짜의 데이터를 모두 가져와 해당 리스트에 담아야 함.
        LocalDate.of(2024, 4, 14),
        LocalDate.of(2024, 4, 25)
    )

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

        if (fitDates.contains(date)) {
            binding.itemDayText.setTextColor(ContextCompat.getColor(context, R.color.black))
            binding.imageViewItemDayCurrentDate.setImageResource(R.drawable.ic_circle)
            binding.imageViewItemDayCurrentDate.visibility = View.VISIBLE
            binding.imageViewItemDayCurrentDate.setColorFilter(ContextCompat.getColor(context, R.color.turquoise))
            binding.imageViewItemDayCurrentDate.alpha = 0.3f
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
