package com.fitmate.fitmate.ui.myfit.list

import android.app.ActionBar.LayoutParams
import android.content.Context
import android.graphics.Color
import android.text.PrecomputedText.Params
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.ArrayAdapter
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.fitmate.fitmate.R
import com.fitmate.fitmate.databinding.ItemForListViewBinding
import com.fitmate.fitmate.databinding.ItemMyFitHistoryBinding
import com.fitmate.fitmate.databinding.ItemProgressBinding
import com.fitmate.fitmate.domain.model.FitHistory
import com.fitmate.fitmate.domain.model.MyFitGroupProgress

class MyFitHistoryViewHolder(
    private val binding: ItemMyFitHistoryBinding,
    private val context: Context,
) : RecyclerView.ViewHolder(binding.root) {

    fun binding(item: FitHistory) {
        binding.data = item
        //내부 리사이클러뷰
        Log.d("testt", "리사이클러뷰 뷰홀더 바인딩부분")

        item.groupList.forEach { text ->
            val textView = ItemForListViewBinding.inflate(LayoutInflater.from(context))
            textView.text1.text = text
            binding.listViewMyFitItemInnerForGroupName.addView(textView.text1)
        }
    }
}