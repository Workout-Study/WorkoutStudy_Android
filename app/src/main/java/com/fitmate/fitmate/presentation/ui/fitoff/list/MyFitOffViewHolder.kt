package com.fitmate.fitmate.presentation.ui.fitoff.list

import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import com.fitmate.fitmate.data.model.dto.GetFitMateList
import com.fitmate.fitmate.data.model.dto.UserResponse
import com.fitmate.fitmate.databinding.ItemFitOffBinding
import com.fitmate.fitmate.domain.model.FitOff
import com.fitmate.fitmate.presentation.ui.fitoff.ViewFitOffFragment
import com.fitmate.fitmate.util.DateParseUtils
import java.time.Instant
import java.time.OffsetDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

class MyFitOffViewHolder(private val binding:ItemFitOffBinding, private val fragment:ViewFitOffFragment, private val fitOffOwnerNameInfo:Any): RecyclerView.ViewHolder(binding.root) {

    fun binding(item: FitOff){
        binding.data = item

        //데이터 닉네임 설정
        when(fitOffOwnerNameInfo){
            is GetFitMateList -> {
                val fitMateData = fitOffOwnerNameInfo.fitMateDetails.filter {
                    it.fitMateUserId == item.userId.toString()
                }[0]
                binding.textViewFitOffItemName.text = fitMateData.fitMateUserNickname
            }
            is UserResponse -> {
                binding.textViewFitOffItemName.text = fitOffOwnerNameInfo.nickname
            }
            else -> {
                //이상한 값이 넘어온 상태임.
            }
        }

        binding.textViewFitOffItemDate.text = convertToYYYYMMDD(DateParseUtils.stringToInstant(item.fitOffStartDate)) + " ~ " + convertToYYYYMMDD(DateParseUtils.stringToInstant(item.fitOffEndDate))
    }

    fun convertToYYYYMMDD(dateStr: Instant): String {
        // 입력 Instant를 한국 시간대로 변환
        val dateTime = dateStr.atZone(ZoneId.of("Asia/Seoul")).toOffsetDateTime()
        // 원하는 형식으로 변환하여 반환
        return dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
    }
}