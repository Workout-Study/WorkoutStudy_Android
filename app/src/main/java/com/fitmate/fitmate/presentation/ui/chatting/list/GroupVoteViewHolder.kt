package com.fitmate.fitmate.presentation.ui.chatting.list

import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.fitmate.fitmate.R
import com.fitmate.fitmate.databinding.ItemVoteBinding
import com.fitmate.fitmate.domain.model.VoteItem
import com.fitmate.fitmate.presentation.ui.chatting.list.adapter.VoteViewPageAdapter
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import java.time.Duration
import java.time.Instant
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

class GroupVoteViewHolder(private val binding: ItemVoteBinding, val fragment: Fragment, val onClick: (VoteItem) -> Unit): RecyclerView.ViewHolder(binding.root){
    fun bind(item: VoteItem) {
        binding.voteItem = item
        binding.buttonItemVoteFitgroupVote.setOnClickListener {
            val dialog = MaterialAlertDialogBuilder(fragment.requireContext(), R.style.Theme_Fitmate_Dialog)
            dialog.run {
                setVoteCustomDialog(item)
            }
        }
    }

    private fun MaterialAlertDialogBuilder.setVoteCustomDialog(item: VoteItem) {
        val dialogView = LayoutInflater.from(fragment.context).inflate(R.layout.dialog_vote_image_slider, null)
        val viewPager: ViewPager2 = dialogView.findViewById(R.id.viewPagerDialogVote)
        val hourDescription: TextView = dialogView.findViewById(R.id.textViewDialogVoteHour)
        val timeDescription: TextView = dialogView.findViewById(R.id.textViewDialogVoteTime)
        val imageUrls = listOf(item.image, item.image, item.image)
        val startTime = Instant.parse(item.startTime).atZone(ZoneId.of("Asia/Seoul"))
        val endTime = Instant.parse(item.endTime).atZone(ZoneId.of("Asia/Seoul"))
        val duration = Duration.between(startTime, endTime)
        val hours = duration.toHours()
        val minutes = duration.toMinutes() % 60

        bindDialog(viewPager, imageUrls, hourDescription, startTime, endTime, timeDescription, hours, minutes, item, dialogView)
    }

    private fun MaterialAlertDialogBuilder.bindDialog(
        viewPager: ViewPager2, imageUrls: List<String>, hourDescription: TextView,
        startTime: ZonedDateTime, endTime: ZonedDateTime, timeDescription: TextView,
        hours: Long, minutes: Long, item: VoteItem, dialogView: View?
    ) {
        viewPager.adapter = VoteViewPageAdapter(imageUrls)
        hourDescription.text = "측정 시각 : ${formatDate(startTime)} ~ ${formatDate(endTime)}"
        timeDescription.text = "총 운동 시간 : ${hours}시간 ${minutes}분"

        setIcon(R.drawable.ic_baseline_timer_24)
        setTitle("${item.fitMate} 님의 운동 기록")
        setView(dialogView)
        setNeutralButton("보류") { dialog, which -> null }
        setNegativeButton("반대") { dialog, which -> /* TODO {"requestUserId":"testUserId","agree":true,"targetCategory":1,"targetId":13} /my-fit-service/votes 으로 false 통신 */ }
        setPositiveButton("찬성") { dialog, which -> /* TODO {"requestUserId":"testUserId","agree":true,"targetCategory":1,"targetId":13} /my-fit-service/votes 으로 true 통신 */ }
        show()
    }

    private fun formatDate(time:ZonedDateTime): String = time.format(DateTimeFormatter.ofPattern("'['MM/dd']' HH:mm"))

}