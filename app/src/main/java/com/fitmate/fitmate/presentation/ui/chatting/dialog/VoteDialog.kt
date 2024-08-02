package com.fitmate.fitmate.presentation.ui.chatting.dialog

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.fitmate.fitmate.R
import com.fitmate.fitmate.databinding.DialogGroupVoteBinding
import com.fitmate.fitmate.databinding.DialogGroupVoteChangeToAgreeBinding
import com.fitmate.fitmate.databinding.DialogGroupVoteChangeToOppositeBinding
import com.fitmate.fitmate.domain.model.GroupVoteCertificationDetail
import com.fitmate.fitmate.domain.model.VoteRequest
import com.fitmate.fitmate.presentation.ui.chatting.list.adapter.VoteImageViewPagerAdapter
import com.fitmate.fitmate.presentation.viewmodel.VoteViewModel
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeFormatterBuilder
import java.time.temporal.ChronoField

class VoteDialog(
    voteFragmentInterface: VoteFragmentInterface,
    viewBinding: ViewDataBinding,
    data: GroupVoteCertificationDetail
):DialogFragment() {
    private var _binding: ViewDataBinding? = null
    private val binding get() = _binding!!

    private val fragmentRemote: VoteFragmentInterface

    private val itemData: GroupVoteCertificationDetail

    init {
        fragmentRemote = voteFragmentInterface
        _binding = viewBinding
        itemData = data
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        //배경 색 변경
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        when(binding){
            is DialogGroupVoteBinding -> {
                (binding as DialogGroupVoteBinding).apply {
                    data = itemData
                    dialog = this@VoteDialog


                    viewPagerGroupVote.adapter = VoteImageViewPagerAdapter(itemData.thumbnailEndPoint)
                    viewPagerGroupVote.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

                    voteOppositeButton.setOnClickListener {
                        val voteRequest = VoteRequest(fragmentRemote.userId, false, 1, itemData.certificationId)
                        fragmentRemote.postVote(voteRequest)
                        dismiss()
                    }

                    voteAgreeButton.setOnClickListener {
                        val voteRequest = VoteRequest(fragmentRemote.userId, true, 1, itemData.certificationId)
                        fragmentRemote.postVote(voteRequest)
                        dismiss()
                    }

                    voteCancelButton.setOnClickListener {
                        dismiss()
                    }
                }
            }
            is DialogGroupVoteChangeToOppositeBinding -> {
                (binding as DialogGroupVoteChangeToOppositeBinding).apply {
                    data = itemData
                    dialog = this@VoteDialog

                    viewPagerGroupVote.adapter = VoteImageViewPagerAdapter(itemData.thumbnailEndPoint)
                    viewPagerGroupVote.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

                    voteCancelButton.setOnClickListener {
                        dismiss()
                    }
                    voteChangeToOppositeButton.setOnClickListener {
                        val voteRequest = VoteRequest(fragmentRemote.userId, false, 1, itemData.certificationId)
                        fragmentRemote.putVote(voteRequest)
                        dismiss()
                    }

                }
            }
            is DialogGroupVoteChangeToAgreeBinding -> {
                (binding as DialogGroupVoteChangeToAgreeBinding).apply {
                    data = itemData
                    dialog = this@VoteDialog

                    viewPagerGroupVote.adapter = VoteImageViewPagerAdapter(itemData.thumbnailEndPoint)
                    viewPagerGroupVote.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

                    voteCancelButton.setOnClickListener {
                        dismiss()
                    }
                    voteChangeToAgreeButton.setOnClickListener {
                        val voteRequest = VoteRequest(fragmentRemote.userId, true, 1, itemData.certificationId)
                        fragmentRemote.putVote(voteRequest)
                        dismiss()
                    }
                }
            }
        }

        return binding.root
    }

    fun formatDateRange(startDate: String, endDate: String): String {
        // 입력과 출력을 위한 날짜-시간 포맷터 정의
        val inputFormatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME
        val dateFormatter = DateTimeFormatter.ofPattern("yyyy/MM/dd")
        val timeFormatter = DateTimeFormatterBuilder()
            .appendPattern("a h:mm")
            .parseCaseInsensitive()
            .parseDefaulting(ChronoField.SECOND_OF_MINUTE, 0)
            .toFormatter()

        // 입력 문자열을 LocalDateTime으로 파싱
        val startDateTime = LocalDateTime.parse(startDate, inputFormatter)
        val endDateTime = LocalDateTime.parse(endDate, inputFormatter)

        // 날짜 부분 포맷팅
        val startDatePart = dateFormatter.format(startDateTime)
        val endDatePart = dateFormatter.format(endDateTime)

        // 시간 부분 포맷팅
        val startTimePart = timeFormatter.format(startDateTime)
        val endTimePart = timeFormatter.format(endDateTime)

        return "$startDatePart $startTimePart ~ $endDatePart $endTimePart"
    }
}