package com.fitmate.fitmate.presentation.ui.chatting.list

import android.content.Context
import android.view.LayoutInflater
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.fitmate.fitmate.R
import com.fitmate.fitmate.databinding.DialogGroupVoteChangeToOppositeBinding
import com.fitmate.fitmate.databinding.ItemVoteBinding
import com.fitmate.fitmate.domain.model.GroupVoteCertificationDetail
import com.fitmate.fitmate.domain.model.VoteItem
import com.fitmate.fitmate.presentation.ui.chatting.list.adapter.VoteViewPageAdapter
import com.fitmate.fitmate.presentation.viewmodel.VoteViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import java.time.Duration
import java.time.Instant
import java.time.OffsetDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

class GroupProcessingVoteViewHolder(
    private val binding: ItemVoteBinding,
    private val fragment: Fragment,
    private val viewModel: VoteViewModel,
    private val onClick: (VoteItem) -> Unit
): VoteBindingViewHolder<ItemVoteBinding>(binding) {

    override fun bind(item: GroupVoteCertificationDetail) {
        binding.viewHolder = this
        binding.data = item
    }

    fun timeUntilEnd(timeString: String): String {
        // Parse the input time string
        val formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME
        val endTime = OffsetDateTime.parse(timeString, formatter)

        // Get current time
        val currentTime = OffsetDateTime.now()

        // Calculate duration between current time and end time
        val duration = Duration.between(currentTime, endTime)

        // Calculate hours and minutes until end time
        val hours = duration.toHours()
        val minutes = duration.toMinutes() % 60

        // Return formatted string based on remaining time
        return when {
            hours > 0 -> "${hours}시간"
            else -> "${minutes}분"
        }
    }


    fun showVoteDialog(item: GroupVoteCertificationDetail) {
        val customView = DialogGroupVoteChangeToOppositeBinding.inflate(LayoutInflater.from(fragment.requireContext()))
        val test = MaterialAlertDialogBuilder(fragment.requireContext()).apply {
            setView(customView.root)
        }
        test.show()
    }

    private fun formatDate(time: ZonedDateTime): String = time.format(DateTimeFormatter.ofPattern("yyyy/MM/dd a hh:mm"))
}
