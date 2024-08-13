package com.fitmate.fitmate.presentation.ui.chatting.list

import android.util.Log
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.fitmate.fitmate.domain.model.GroupVoteCertificationDetail
import com.fitmate.fitmate.util.DateParseUtils
import java.time.Duration
import java.time.Instant
import java.time.LocalDateTime
import java.time.OffsetDateTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

open class VoteBindingViewHolder<VB : ViewDataBinding>(private val binding: VB) :
    RecyclerView.ViewHolder(binding.root) {

    protected var item: GroupVoteCertificationDetail? = null

    open fun bind(item: GroupVoteCertificationDetail) {
        this.item = item
    }

    open fun timeUntilEnd(timeString: String): String {
        // 한국 시간대
        val koreaZone = ZoneId.of("Asia/Seoul")

        val endTime = LocalDateTime.ofInstant(DateParseUtils.stringToInstant(timeString), koreaZone)

        // 현재 시간을 한국 시간으로 변환
        val nowKoreaTime = LocalDateTime.now(koreaZone)

        // 현재 시간과 주어진 시간의 차이 계산
        val minutesUntil = ChronoUnit.MINUTES.between(nowKoreaTime, endTime)

        return if (minutesUntil >= 60) {
            val hoursUntil = minutesUntil / 60
            "${hoursUntil}시간"
        } else {
            "${minutesUntil}분"
        }
    }
}

