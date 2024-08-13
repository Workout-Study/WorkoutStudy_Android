package com.fitmate.fitmate.presentation.ui.chatting.list.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.fitmate.fitmate.R

class VoteImageViewPagerAdapter(private val imageUrls: List<String>?) : RecyclerView.Adapter<VoteImageViewPagerAdapter.SliderViewHolder>() {

    inner class SliderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.imageView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SliderViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_image, parent, false)
        return SliderViewHolder(view)
    }

    override fun onBindViewHolder(holder: SliderViewHolder, position: Int) {
        imageUrls?.let {
            Glide.with(holder.itemView.context).load(imageUrls[position]).into(holder.imageView)
        }
    }

    override fun getItemCount(): Int {
        imageUrls?.let {
            return imageUrls.size
        }
        return 0
    }
}