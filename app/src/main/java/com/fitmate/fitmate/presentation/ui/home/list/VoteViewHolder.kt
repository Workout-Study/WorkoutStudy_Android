package com.fitmate.fitmate.presentation.ui.home.list

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.fitmate.fitmate.R
import com.fitmate.fitmate.databinding.ItemMyGroupNewsBinding
import com.fitmate.fitmate.databinding.ItemVoteBinding
import com.fitmate.fitmate.domain.model.MyGroupNews
import com.fitmate.fitmate.domain.model.VoteItem
import com.fitmate.fitmate.presentation.viewmodel.HomeViewModel
import com.fitmate.fitmate.util.DateParseUtils
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.temporal.ChronoUnit

class VoteViewHolder(private val binding: ItemMyGroupNewsBinding, private val fragment: Fragment, private val viewModel: HomeViewModel, val onClick: (MyGroupNews) -> Unit) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(item: MyGroupNews) {
        binding.data = item

        //그룹 이름 매핑
        val fitGroupNameByFilterGroupId = viewModel.myFitGroupList.value?.filter {
            it.fitGroupId == item.fitGroupId.toString()
        }
        val groupName = fitGroupNameByFilterGroupId?.get(0)?.fitGroupName
        binding.textViewItemVoteFitgroupTitle.text = groupName

        //찬성률, 진행률 설정
        try {
            val agreePercent = ((item.agreeCount / (item.agreeCount + item.disagreeCount.toFloat())) * 100).toInt()
            binding.textViewVoteAgreePercent.text = fragment.context?.getString(R.string.vote_agree_percent, agreePercent)
        }catch (e:ArithmeticException){
            binding.textViewVoteAgreePercent.text = fragment.context?.getString(R.string.vote_agree_percent,0)
        }
        try {
            val progressPercent = (((item.agreeCount + item.disagreeCount) / item.maxAgreeCount.toFloat()) * 100).toInt()
            binding.textViewVoteProgressPercent.text = fragment.context?.getString(R.string.vote_agree_percent, progressPercent)
        }catch (e:ArithmeticException){
            binding.textViewVoteProgressPercent.text = fragment.context?.getString(R.string.vote_agree_percent,0)
        }

        //시간 설정
        binding.textViewItemVoteFitGroupTime.text = convertAndCompareTime(item.issueDate)
    }

    private fun convertAndCompareTime(issueDate:String):String {
        // 한국 시간대
        val koreaZone = ZoneId.of("Asia/Seoul")

        // 주어진 Instant 값을 한국 시간으로 변환
        val targetTime = LocalDateTime.ofInstant(DateParseUtils.stringToInstant(issueDate), koreaZone)

        // 현재 시간을 한국 시간으로 변환
        val nowKoreaTime = LocalDateTime.now(koreaZone)

        // 주어진 시간과 현재 시간의 차이 계산
        val minutesDifference = ChronoUnit.MINUTES.between(nowKoreaTime, targetTime)

        return if (-minutesDifference >= 60) {
            val hoursDifference = -minutesDifference / 60
            "$hoursDifference 시간 전"
        } else if (-minutesDifference > 0) {
            "${-minutesDifference} 분 전"
        } else {
            "방금 전"
        }
    }
}
