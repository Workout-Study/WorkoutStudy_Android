package com.fitmate.fitmate.presentation.ui.point.list

import androidx.recyclerview.widget.RecyclerView
import com.fitmate.fitmate.databinding.ItemCategoryBinding
import com.fitmate.fitmate.databinding.ItemPointHistoryBinding
import com.fitmate.fitmate.domain.model.CategoryItem
import com.fitmate.fitmate.domain.model.PointHistoryContent
import com.fitmate.fitmate.util.DateParseUtils
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

class PointHistoryViewHolder(private val binding: ItemPointHistoryBinding): RecyclerView.ViewHolder(binding.root) {
    fun bind(item: PointHistoryContent) {
        binding.data = item
        binding.viewHolder = this
    }

    fun formatDateTime(dateStr: String): String {
        // 입력 Instant를 한국 시간대로 변환
        val dateTime = DateParseUtils.stringToInstant(dateStr).atZone(ZoneId.of("Asia/Seoul")).toOffsetDateTime()
        // 원하는 형식으로 변환하여 반환
        return dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
    }
}