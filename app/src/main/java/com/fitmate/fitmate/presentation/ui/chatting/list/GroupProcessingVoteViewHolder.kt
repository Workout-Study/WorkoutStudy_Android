package com.fitmate.fitmate.presentation.ui.chatting.list

import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import com.fitmate.fitmate.databinding.DialogGroupVoteBinding
import com.fitmate.fitmate.databinding.ItemVoteBinding
import com.fitmate.fitmate.domain.model.GroupVoteCertificationDetail
import com.fitmate.fitmate.presentation.ui.chatting.dialog.VoteDialog
import com.fitmate.fitmate.presentation.ui.chatting.dialog.VoteFragmentInterface
import com.fitmate.fitmate.presentation.viewmodel.VoteViewModel
import java.time.Duration
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter

class GroupProcessingVoteViewHolder(
    private val binding: ItemVoteBinding,
    private val fragment: Fragment,
    private val voteFragmentInterface: VoteFragmentInterface,
    private val viewModel: VoteViewModel,
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
        val customView = DialogGroupVoteBinding.inflate(LayoutInflater.from(fragment.requireContext()))
        val dialog = VoteDialog(voteFragmentInterface,customView,item)
        val fragmentManager = fragment.activity?.supportFragmentManager
        dialog.show(fragmentManager!!,"VoteDialog")
    }

}
