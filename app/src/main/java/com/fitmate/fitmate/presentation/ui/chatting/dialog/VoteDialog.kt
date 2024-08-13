package com.fitmate.fitmate.presentation.ui.chatting.dialog

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
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
import com.fitmate.fitmate.util.DateParseUtils
import java.time.LocalDateTime
import java.time.OffsetDateTime
import java.time.ZoneId
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
        Log.d("tlqkf",itemData.multiMediaEndPoints.toString())
        when(binding){
            is DialogGroupVoteBinding -> {
                (binding as DialogGroupVoteBinding).apply {
                    data = itemData
                    dialog = this@VoteDialog


                    viewPagerGroupVote.adapter = VoteImageViewPagerAdapter(itemData.multiMediaEndPoints)
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

                    viewPagerGroupVote.adapter = VoteImageViewPagerAdapter(itemData.multiMediaEndPoints)
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

                    viewPagerGroupVote.adapter = VoteImageViewPagerAdapter(itemData.multiMediaEndPoints)
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
        // 한국 시간대
        val koreaZone = ZoneId.of("Asia/Seoul")

        //한국 시간으로 변환
        val startDateToInstant = LocalDateTime.ofInstant(DateParseUtils.stringToInstant(startDate),koreaZone)
        val endDateToInstant = LocalDateTime.ofInstant(DateParseUtils.stringToInstant(endDate),koreaZone)

        // 날짜 및 시간 형식 설정
        val dateFormatter = DateTimeFormatter.ofPattern("yyyy/MM/dd a h:mm")
        val timeFormatter = DateTimeFormatter.ofPattern("a h:mm")

        // 형식에 맞게 변환
        val startFormatted = startDateToInstant.format(dateFormatter)
        val endFormatted = endDateToInstant.format(timeFormatter)

        // 결과 문자열 반환
        return "$startFormatted ~ $endFormatted"
    }
}