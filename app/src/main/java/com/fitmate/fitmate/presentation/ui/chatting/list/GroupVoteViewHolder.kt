package com.fitmate.fitmate.presentation.ui.chatting.list

import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.fitmate.fitmate.R
import com.fitmate.fitmate.data.model.dto.VoteRequest
import com.fitmate.fitmate.databinding.ItemVoteBinding
import com.fitmate.fitmate.domain.model.VoteItem
import com.fitmate.fitmate.presentation.ui.chatting.list.adapter.VoteViewPageAdapter
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
        val currentTime = ZonedDateTime.now()
        val endTime = ZonedDateTime.parse(item.endTime)
        val isExpired = endTime.isBefore(currentTime)

        binding.voteItem = item

        if (isExpired) {
            binding.root.alpha = 0.5f
            binding.buttonItemVoteFitgroupVote.visibility = View.GONE
            val params = binding.root.layoutParams as ConstraintLayout.LayoutParams
            val scale = binding.root.context.resources.displayMetrics.density
            params.height = (105 * scale + 0.5f).toInt()
            binding.root.layoutParams = params
        } else {
            binding.root.alpha = 1.0f
            binding.buttonItemVoteFitgroupVote.visibility = View.VISIBLE
            val params = binding.root.layoutParams as ConstraintLayout.LayoutParams
            params.height = ConstraintLayout.LayoutParams.WRAP_CONTENT
            binding.root.layoutParams = params
        }

        Glide.with(binding.imageViewItemCategoryFitgroupThumbnail)
            .load(item.image)
            .transform(CenterCrop(), RoundedCorners(16))
            .error(R.drawable.ic_launcher_logo)
            .into(binding.imageViewItemCategoryFitgroupThumbnail)
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
        val dialogView = LayoutInflater.from(fragment.context).inflate(R.layout.dialog_group_vote, null)
        val imageUrls = listOf(item.image, item.image, item.image)
        val startTime = Instant.parse(item.startTime).atZone(ZoneId.of("Asia/Seoul"))
        val endTime = Instant.parse(item.endTime).atZone(ZoneId.of("Asia/Seoul"))
        val duration = Duration.between(startTime, endTime)
        val hours = duration.toHours()
        val minutes = duration.toMinutes() % 60

        val title = dialogView.findViewById<TextView>(R.id.buttonGroupVoteFitMate)
        val retrain = dialogView.findViewById<Button>(R.id.buttonGroupVoteRetrain)
        val disAgree = dialogView.findViewById<Button>(R.id.buttonGroupVoteDisagree)
        val agree = dialogView.findViewById<Button>(R.id.buttonGroupVoteAgree)
        val textDuration = dialogView.findViewById<TextView>(R.id.textGroupVoteDuration)
        val category = dialogView.findViewById<TextView>(R.id.textGroupVoteCategory)
        val viewPager = dialogView.findViewById<ViewPager2>(R.id.viewPagerGroupVote)

        val dialog = this.setView(dialogView).create().apply {
            title.text = "${item.fitMate} 님의 운동기록에 투표하세요!"
            textDuration.text = formatDate(startTime) + " ~ " + formatDate(endTime)
            category.text = "${hours}시간 ${minutes}분"
            viewPager.adapter = VoteViewPageAdapter(imageUrls)

            retrain.setOnClickListener { dismiss() }
            disAgree.setOnClickListener {
                fragment.lifecycleScope.launch {
                    viewModel.registerVote(VoteRequest(item.fitMate.toInt(), true, 1, item.groupId))
                }
                dismiss()
            }
            agree.setOnClickListener {
                fragment.lifecycleScope.launch {
                    viewModel.registerVote(VoteRequest(item.fitMate.toInt(), false, 1, item.groupId))
                }
                dismiss()
            }
        }
        dialog.show()
    }
    private fun formatDate(time: ZonedDateTime): String = time.format(DateTimeFormatter.ofPattern("yyyy/MM/dd a hh:mm"))
}
