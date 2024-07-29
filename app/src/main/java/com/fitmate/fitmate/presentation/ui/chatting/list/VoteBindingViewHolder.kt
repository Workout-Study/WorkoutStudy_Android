package com.fitmate.fitmate.presentation.ui.chatting.list

import androidx.databinding.ViewDataBinding
import androidx.databinding.library.baseAdapters.BR
import androidx.recyclerview.widget.RecyclerView
import com.fitmate.fitmate.domain.model.GroupVoteCertificationDetail

open class VoteBindingViewHolder<VB : ViewDataBinding>(private val binding: VB) :
    RecyclerView.ViewHolder(binding.root) {

    protected var item: GroupVoteCertificationDetail? = null

    open fun bind(item: GroupVoteCertificationDetail) {
        this.item = item
    }

    open fun calculateVoteProcess(): String {
        item.let {
            return if (it != null) ((it.agreeCount.toDouble() / it.maxAgreeCount) * 100).toInt().toString() else "0"
        }
    }
}

