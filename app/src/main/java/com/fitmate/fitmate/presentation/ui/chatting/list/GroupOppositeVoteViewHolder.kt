package com.fitmate.fitmate.presentation.ui.chatting.list

import android.view.LayoutInflater
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.fitmate.fitmate.R
import com.fitmate.fitmate.databinding.DialogGroupVoteChangeToAgreeBinding
import com.fitmate.fitmate.databinding.DialogGroupVoteChangeToOppositeBinding
import com.fitmate.fitmate.databinding.ItemAgreeVoteBinding
import com.fitmate.fitmate.databinding.ItemOppositeVoteBinding
import com.fitmate.fitmate.domain.model.GroupVoteCertificationDetail
import com.fitmate.fitmate.domain.model.VoteItem
import com.fitmate.fitmate.presentation.ui.chatting.dialog.VoteDialog
import com.fitmate.fitmate.presentation.ui.chatting.dialog.VoteFragmentInterface
import com.fitmate.fitmate.presentation.ui.chatting.list.adapter.VoteViewPageAdapter
import com.fitmate.fitmate.presentation.viewmodel.VoteViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import java.time.Duration
import java.time.Instant
import java.time.OffsetDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

class GroupOppositeVoteViewHolder(
    private val binding: ItemOppositeVoteBinding,
    private val fragment: Fragment,
    private val voteFragmentInterface: VoteFragmentInterface,
    private val viewModel: VoteViewModel,
): VoteBindingViewHolder<ItemOppositeVoteBinding>(binding) {

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
        val customView = DialogGroupVoteChangeToAgreeBinding.inflate(LayoutInflater.from(fragment.requireContext()))
        val dialog = VoteDialog(voteFragmentInterface,customView,item)
        val fragmentManager = fragment.activity?.supportFragmentManager
        dialog.show(fragmentManager!!,"VoteDialog")
    }
}
