package com.fitmate.fitmate.presentation.ui.fitoff.list

import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import com.fitmate.fitmate.data.model.dto.GetFitMateList
import com.fitmate.fitmate.data.model.dto.UserResponse
import com.fitmate.fitmate.databinding.ItemFitOffBinding
import com.fitmate.fitmate.domain.model.FitOff
import com.fitmate.fitmate.presentation.ui.fitoff.ViewFitOffFragment

class MyFitOffViewHolder(private val binding:ItemFitOffBinding, private val fragment:ViewFitOffFragment, private val fitOffOwnerNameInfo:Any): RecyclerView.ViewHolder(binding.root) {

    fun binding(item: FitOff){
        binding.data = item

        //데이터 닉네임 설정
        when(fitOffOwnerNameInfo){
            is GetFitMateList -> {
                val fitMateData = fitOffOwnerNameInfo.fitMateDetails.filter {
                    it.fitMateId == item.userId
                }[0]
                binding.textViewFitOffItemName.text = fitMateData.fitMateUserNickname
            }
            is UserResponse -> {
                binding.textViewFitOffItemName.text = fitOffOwnerNameInfo.nickname
            }
            else -> {
                Log.d("tlqkf","어떠한 데이터도 아님")
            }
        }
    }
}