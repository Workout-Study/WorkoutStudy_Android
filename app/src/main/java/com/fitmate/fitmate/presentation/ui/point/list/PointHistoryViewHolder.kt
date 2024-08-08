package com.fitmate.fitmate.presentation.ui.point.list

import androidx.recyclerview.widget.RecyclerView
import com.fitmate.fitmate.databinding.ItemCategoryBinding
import com.fitmate.fitmate.databinding.ItemPointHistoryBinding
import com.fitmate.fitmate.domain.model.CategoryItem
import com.fitmate.fitmate.domain.model.PointHistoryContent
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

class PointHistoryViewHolder(private val binding: ItemPointHistoryBinding): RecyclerView.ViewHolder(binding.root) {
    fun bind(item: PointHistoryContent) {
        binding.data = item
        binding.viewHolder = this
    }

    fun formatDateTime(input: String): String {
        // 날짜와 시간 포맷 정의
        val inputFormatter = DateTimeFormatter.ISO_DATE_TIME
        val outputFormatterDate = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val outputFormatterTime = DateTimeFormatter.ofPattern("a hh:mm")

        // 문자열을 LocalDateTime으로 변환
        val dateTimeUTC = LocalDateTime.parse(input, inputFormatter)

        // 한국 시간대 (KST)로 변환
        val zoneIdKST = ZoneId.of("Asia/Seoul")
        val zonedDateTimeKST = dateTimeUTC.atZone(ZoneId.of("UTC")).withZoneSameInstant(zoneIdKST)

        // 현재 시간의 한국 시간대 가져오기
        val nowInKST = ZonedDateTime.now(zoneIdKST)

        // 현재 날짜
        val todayInKST = nowInKST.toLocalDate()

        // 비교할 날짜
        val dateOfInputInKST = zonedDateTimeKST.toLocalDate()

        // 현재 날짜와 입력된 날짜가 같은 경우
        return if (dateOfInputInKST.isEqual(todayInKST)) {
            // 오늘 날짜의 시간 부분만 가져와서 포맷
            zonedDateTimeKST.format(outputFormatterTime)
        } else {
            // 오늘 날짜가 아닌 경우
            dateOfInputInKST.format(outputFormatterDate)
        }
    }
}