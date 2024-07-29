package com.fitmate.fitmate.presentation.ui.chatting.list

import android.view.LayoutInflater
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.fitmate.fitmate.R
import com.fitmate.fitmate.databinding.DialogGroupVoteChangeToOppositeBinding
import com.fitmate.fitmate.databinding.ItemAgreeVoteBinding
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

class GroupAgreeVoteViewHolder(
    private val binding: ItemAgreeVoteBinding,
    private val fragment: Fragment,
    private val viewModel: VoteViewModel,
    private val onClick: (VoteItem) -> Unit
): VoteBindingViewHolder<ItemAgreeVoteBinding>(binding) {

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
        MaterialAlertDialogBuilder(fragment.requireContext(), R.style.Theme_Fitmate_Dialog).apply {
            setVoteCustomDialog(item)
        }.show()
    }

    private fun MaterialAlertDialogBuilder.setVoteCustomDialog(item: GroupVoteCertificationDetail) {
        //val binding = DialogGroupVoteChangeToOppositeBinding.inflate(LayoutInflater.from(this.context))
        val dialogView = LayoutInflater.from(fragment.context).inflate(R.layout.dialog_group_vote_change_to_opposite, null)
        val startTime = Instant.parse(item.fitRecordStartDate).atZone(ZoneId.of("Asia/Seoul"))
        val endTime = Instant.parse(item.fitRecordEndDate).atZone(ZoneId.of("Asia/Seoul"))
        val duration = Duration.between(startTime, endTime)
        val hours = duration.toHours()
        val minutes = duration.toMinutes() % 60
        val title = dialogView.findViewById<TextView>(R.id.buttonGroupVoteFitMate)
        val buttonGroupVoteChangeToOpposite = dialogView.findViewById<TextView>(R.id.buttonGroupVoteChangeToOpposite)

        val textDuration = dialogView.findViewById<TextView>(R.id.textGroupVoteDuration)
        val category = dialogView.findViewById<TextView>(R.id.textGroupVoteCategory)
        val viewPager = dialogView.findViewById<ViewPager2>(R.id.viewPagerGroupVote)

        val dialog = this.setView(dialogView).create().apply {
            title.text = "${item.certificationRequestUserNickname} 님의 운동기록에 투표하세요!"
            textDuration.text = formatDate(startTime) + " ~ " + formatDate(endTime)
            category.text = "${hours}시간 ${minutes}분"
            viewPager.adapter = VoteViewPageAdapter(item.thumbnailEndPoint)


            buttonGroupVoteChangeToOpposite.setOnClickListener {
/*                fragment.lifecycleScope.launch {
                    viewModel.registerVote(VoteRequestDto(item.fitMate.toInt(), true, 1, item.groupId))
                }*/
                dismiss()
            }
        }
        dialog.show()
    }
    private fun formatDate(time: ZonedDateTime): String = time.format(DateTimeFormatter.ofPattern("yyyy/MM/dd a hh:mm"))
}
