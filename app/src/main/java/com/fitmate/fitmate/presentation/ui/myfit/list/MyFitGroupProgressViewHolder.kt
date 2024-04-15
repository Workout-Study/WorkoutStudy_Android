package com.fitmate.fitmate.ui.myfit.list

import android.content.Context
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.RecyclerView
import com.fitmate.fitmate.R
import com.fitmate.fitmate.databinding.ItemProgressBinding
import com.fitmate.fitmate.domain.model.FitProgressItem

class MyFitGroupProgressViewHolder(private val binding:ItemProgressBinding, private val context:Context): RecyclerView.ViewHolder(binding.root) {

    fun binding(item: FitProgressItem){
        val animation = AnimationUtils.loadAnimation(context, R.anim.progress_fill_animation)
        binding.progressBarItemProgress.startAnimation(animation)

/*        val animator = ObjectAnimator.ofInt(binding.progressBarItemProgress, "progress", binding.progressBarItemProgress.progress, item.certificationCount)
        animator.duration = 500 // 애니메이션 지속 시간 설정 (ms)
        animator.start()*/

        binding.data = item
    }
}