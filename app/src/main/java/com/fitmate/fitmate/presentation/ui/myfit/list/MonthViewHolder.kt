package com.fitmate.fitmate.presentation.ui.myfit.list

// MonthViewHolder.kt
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.fitmate.fitmate.databinding.ItemListMonthBinding
import com.fitmate.fitmate.presentation.ui.myfit.list.adapter.DayListAdapter
import java.time.LocalDate
import java.time.ZoneId
import java.util.Calendar
import java.util.Date

abstract class MonthViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    abstract fun bind(month: Int, position: Int)
}

class MonthView(
    private val binding: ItemListMonthBinding
) : MonthViewHolder(binding.root) {
    private val calendar: Calendar = Calendar.getInstance()

    override fun bind(month: Int, position: Int) {
        calendar.time = Date()
        calendar.set(Calendar.DAY_OF_MONTH, 1)
        calendar.add(Calendar.MONTH, month)
        binding.itemMonthText.text = "${calendar.get(Calendar.YEAR)}년 ${calendar.get(Calendar.MONTH) + 1}월"
        val tempMonth = calendar.get(Calendar.MONTH) + 1

        var dayList: MutableList<LocalDate> = MutableList(6 * 7) { LocalDate.now() }
        for (i in 0..5) {
            for (k in 0..6) {
                calendar.add(
                    Calendar.DAY_OF_MONTH,
                    (1 - calendar.get(Calendar.DAY_OF_WEEK)) + k
                )
                dayList[i * 7 + k] =
                    calendar.time.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
            }
            calendar.add(Calendar.WEEK_OF_MONTH, 1)
        }
        val dayListManager = GridLayoutManager(binding.root.context, 7)
        val dayListAdapter = DayListAdapter(tempMonth, dayList) { position -> }
        binding.itemMonthDayList.apply {
            layoutManager = dayListManager
            adapter = dayListAdapter
        }
    }
}
