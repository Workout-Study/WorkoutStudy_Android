package com.fitmate.fitmate.presentation.ui.chatting.list

import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.fitmate.fitmate.R
import com.fitmate.fitmate.data.model.dto.VoteRequest
import com.fitmate.fitmate.databinding.ItemVoteBinding
import com.fitmate.fitmate.domain.model.VoteItem
import com.fitmate.fitmate.presentation.ui.chatting.list.adapter.VoteViewPageAdapter
import com.fitmate.fitmate.domain.usecase.GroupUseCase
import com.fitmate.fitmate.presentation.viewmodel.GroupViewModel
import com.fitmate.fitmate.presentation.viewmodel.VoteViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.launch
import java.time.Duration
import java.time.Instant
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

class GroupVoteViewHolder(
    private val binding: ItemVoteBinding,
    private val fragment: Fragment,
    private val viewModel: VoteViewModel,
    private val onClick: (VoteItem) -> Unit
): RecyclerView.ViewHolder(binding.root) {

    init {
        setupListeners()
    }

    fun bind(item: VoteItem) {
        binding.voteItem = item
    }

    private fun setupListeners() {
        binding.buttonItemVoteFitgroupVote.setOnClickListener {
            showVoteDialog(binding.voteItem!!)
        }
    }

    private fun showVoteDialog(item: VoteItem) {
        MaterialAlertDialogBuilder(fragment.requireContext(), R.style.Theme_Fitmate_Dialog).apply {
            setVoteCustomDialog(item)
        }.show()
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

        viewPager.adapter = VoteViewPageAdapter(imageUrls)
        hourDescription.text = formatDate(startTime) + " ~ " + formatDate(endTime)
        timeDescription.text = "${hours}시간 ${minutes}분"

        setIcon(R.drawable.ic_baseline_timer_24)
        setTitle("${item.fitMate} 님의 운동 기록")
        setView(dialogView)
        setNeutralButton("보류") { dialog, which -> }
        setNegativeButton("반대") { dialog, which ->
            fragment.lifecycleScope.launch {
                viewModel.registerVote(VoteRequest(item.fitMate, false, 1, item.groupId))
            }
        }
        setPositiveButton("찬성") { dialog, which ->
            fragment.lifecycleScope.launch {
                viewModel.registerVote(VoteRequest(item.fitMate, true, 1, item.groupId))
            }
        }
    }

    private fun formatDate(time: ZonedDateTime): String =
        time.format(DateTimeFormatter.ofPattern("'['MM/dd']' HH:mm"))
}
