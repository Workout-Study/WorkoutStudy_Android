package com.fitmate.fitmate.presentation.ui.chatting.list

import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.fitmate.fitmate.domain.model.GroupVoteCertificationDetail
import java.time.Duration
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter

open class VoteBindingViewHolder<VB : ViewDataBinding>(private val binding: VB) :
    RecyclerView.ViewHolder(binding.root) {

    protected var item: GroupVoteCertificationDetail? = null

    open fun bind(item: GroupVoteCertificationDetail) {
        this.item = item
    }

    open fun timeUntilEnd(timeString: String): String {
        val formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME
        val endTime = OffsetDateTime.parse(timeString, formatter)

        val currentTime = OffsetDateTime.now()

        val duration = Duration.between(currentTime, endTime)


        val hours = duration.toHours()
        val minutes = duration.toMinutes() % 60

        return when {
            hours > 0 -> "${hours}시간"
            else -> "${minutes}분"
        }
    }
}

