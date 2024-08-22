package com.fitmate.fitmate.presentation.ui.point.list

import androidx.recyclerview.widget.RecyclerView
import com.fitmate.fitmate.R
import com.fitmate.fitmate.data.model.dto.GetFitMateList
import com.fitmate.fitmate.data.model.dto.UserResponse
import com.fitmate.fitmate.databinding.ItemCategoryBinding
import com.fitmate.fitmate.databinding.ItemPointHistoryBinding
import com.fitmate.fitmate.domain.model.CategoryItem
import com.fitmate.fitmate.domain.model.PointHistoryContent
import com.fitmate.fitmate.util.DateParseUtils
import com.fitmate.fitmate.util.setImageByUri
import com.fitmate.fitmate.util.setImageByUrl
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

class PointHistoryViewHolder(
    private val binding: ItemPointHistoryBinding,
    private var fitMateData: GetFitMateList? = null,
    private var userImage: UserResponse? = null
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(item: PointHistoryContent) {
        binding.data = item
        binding.viewHolder = this

        if (fitMateData != null) {//그룹 포인트 내역일 경우
            if (item.depositUserId != null) {
                val userImageUrl =
                    fitMateData!!.fitMateDetails.find { it.fitMateUserId == item.depositUserId.toString() }?.fitMateUserProfileImageUrl
                binding.imageViewItemPointHistoryOwnerThumbnail.setImageByUrl(userImageUrl)
            } else {
                binding.imageViewItemPointHistoryOwnerThumbnail.setImageByUrl(null)
            }
        } else {//개인 포인트 내역일 경우
            if (item.depositUserId != null) {
                binding.imageViewItemPointHistoryOwnerThumbnail.setImageByUrl(null)
            } else {
                userImage?.let {
                    binding.imageViewItemPointHistoryOwnerThumbnail.setImageByUrl(userImage?.imageUrl)
                }
            }

            if (item.depositUserNickname.isNullOrBlank()) {
                binding.textViewItemPointHistoryOwnerNickName.text = "피트메이트 관리자"
            } else {
                binding.textViewItemPointHistoryOwnerNickName.text = item.depositUserNickname
            }
        }

        //입금 출금 여부 및 변동 포인트 값 설정
        when (item.tradeType) {
            "DEPOSIT" -> binding.textViewItemPointHistoryValue.text =
                this.itemView.context.getString(R.string.point_amount_deposit, item.amount)

            "WITHDRAW" -> binding.textViewItemPointHistoryValue.text =
                this.itemView.context.getString(R.string.point_amount_withdraw, item.amount)
        }

    }

    fun formatDateTime(dateStr: String): String {
        // 입력 Instant를 한국 시간대로 변환
        val dateTime = DateParseUtils.stringToInstant(dateStr).atZone(ZoneId.of("Asia/Seoul"))
            .toOffsetDateTime()
        // 원하는 형식으로 변환하여 반환
        return dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
    }

}