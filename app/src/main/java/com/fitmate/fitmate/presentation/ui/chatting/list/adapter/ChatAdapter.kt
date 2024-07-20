package com.fitmate.fitmate.presentation.ui.chatting.list.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.fitmate.fitmate.databinding.ItemChattingChatBinding
import com.fitmate.fitmate.domain.model.ChatItem

class ChatAdapter: ListAdapter<ChatItem, ChatAdapter.ViewHolder>(ChatDiffCallback) {

    var currentUserFitMateId: Int? = null

    fun setCurrentUserFitMateId(fitMateId: Int) {
        currentUserFitMateId = fitMateId
    }

    inner class ViewHolder(private val binding: ItemChattingChatBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ChatItem) {
            val isCurrentUser = item.userId == currentUserFitMateId
            binding.apply {
                containerItemChattingChatLeft.visibility = if (isCurrentUser) View.GONE else View.VISIBLE
                containerItemChattingChatRight.visibility = if (isCurrentUser) View.VISIBLE else View.GONE

                val textViewToUse = if (isCurrentUser) textViewItemChattingMySpeechContentRight else textViewItemChattingFitMateSpeechContentLeft
                textViewToUse.text = item.message

                if (!isCurrentUser) {
                    textViewItemChattingFitMateNameLeft.text = item.userId.toString()
                }
            }
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