package com.fitmate.fitmate.presentation.ui.chatting.list.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.fitmate.fitmate.data.model.dto.GetFitMateList
import com.fitmate.fitmate.databinding.ItemChattingChatBinding
import com.fitmate.fitmate.domain.model.ChatItem
import com.fitmate.fitmate.util.DateParseUtils
import com.fitmate.fitmate.util.setImageByUrl
import java.time.ZoneId
import java.time.format.DateTimeFormatter

class ChatAdapter: ListAdapter<ChatItem, ChatAdapter.ViewHolder>(ChatDiffCallback) {

    var currentUserFitMateId: Int? = null
    private lateinit var fitMateData: GetFitMateList

    fun setCurrentUserFitMateId(fitMateId: Int) {
        currentUserFitMateId = fitMateId
    }

    fun setFitMateInfo(fitMateData: GetFitMateList) {
        this.fitMateData = fitMateData
    }

    inner class ViewHolder(private val binding: ItemChattingChatBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ChatItem) {
            val isCurrentUser = item.userId == currentUserFitMateId
            binding.apply {
                if (currentList.size >= 1){
                    try {
                        if (this@ViewHolder.bindingAdapterPosition == 0){
                            textViewChangeDateGuide.visibility = View.VISIBLE
                            textViewChangeDateGuide.text = formatDateTime(DateParseUtils.instantToString(item.messageTime),true)
                        }
                        else if (formatDateTime(DateParseUtils.instantToString(currentList[bindingAdapterPosition - 1].messageTime),true) != formatDateTime(DateParseUtils.instantToString(item.messageTime),true)){
                            textViewChangeDateGuide.visibility = View.VISIBLE
                            textViewChangeDateGuide.text = formatDateTime(DateParseUtils.instantToString(item.messageTime),true)
                        }
                    }catch (e:IndexOutOfBoundsException){
                        textViewChangeDateGuide.visibility = View.VISIBLE
                        textViewChangeDateGuide.text = formatDateTime(DateParseUtils.instantToString(item.messageTime),true)
                    }
                }

                containerItemChattingChatLeft.visibility = if (isCurrentUser) View.GONE else View.VISIBLE
                containerItemChattingChatRight.visibility = if (isCurrentUser) View.VISIBLE else View.GONE

                if (isCurrentUser) {
                    textViewItemChattingMySpeechContentRight.text = item.message
                    textViewItemChattingFitMateSpeechDateRight.text = formatDateTime(DateParseUtils.instantToString(item.messageTime),false)
                } else{
                    textViewItemChattingFitMateSpeechContentLeft.text = item.message
                }

                if (!isCurrentUser && ::fitMateData.isInitialized) {
                    val userInfo = fitMateData.fitMateDetails.find { it.fitMateUserId == item.userId.toString() }
                    imageViewItemChattingFitMateImageLeft.setImageByUrl(userInfo?.fitMateUserProfileImageUrl)
                    textViewItemChattingFitMateNameLeft.text = userInfo?.fitMateUserNickname
                    textViewItemChattingFitMateSpeechDateLeft.text = formatDateTime(DateParseUtils.instantToString(item.messageTime),false)
                }
            }
        }

        private fun formatDateTime(createDate: String, dateVisible:Boolean): String {
            // 한국 시간대
            val koreaZone = ZoneId.of("Asia/Seoul")

            // 문자열을 Instant로 변환
            val instant = DateParseUtils.stringToInstant(createDate)

            // Instant를 한국 시간대로 변환
            val dateToInstant = instant.atZone(koreaZone).toLocalDateTime()

            // 날짜 및 시간 형식 설정
            val dateFormatter = if (dateVisible) DateTimeFormatter.ofPattern("yyyy/MM/dd") else DateTimeFormatter.ofPattern("a h:mm")

            // 형식에 맞게 변환
            val dateFormatted = dateToInstant.format(dateFormatter)

            // 결과 문자열 반환
            return dateFormatted
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemChattingChatBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

    companion object {
        val ChatDiffCallback = object: DiffUtil.ItemCallback<ChatItem>() {
            override fun areItemsTheSame(oldItem: ChatItem, newItem: ChatItem): Boolean {
                return oldItem.messageId == newItem.messageId
            }

            override fun areContentsTheSame(oldItem: ChatItem, newItem: ChatItem): Boolean {
                return oldItem == newItem
            }
        }
    }
}