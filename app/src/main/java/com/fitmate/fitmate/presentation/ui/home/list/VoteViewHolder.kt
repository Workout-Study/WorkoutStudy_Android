package com.fitmate.fitmate.presentation.ui.home.list

import android.os.Bundle
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

class VoteViewHolder(private val binding: ItemMyGroupNewsBinding, private val fragment: Fragment, val onClick: (MyGroupNews) -> Unit) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(item: MyGroupNews) {
        binding.data = item
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
    }
}
