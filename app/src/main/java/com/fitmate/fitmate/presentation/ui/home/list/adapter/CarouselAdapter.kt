package com.fitmate.fitmate.presentation.ui.home.list.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.fitmate.fitmate.R
import com.fitmate.fitmate.databinding.ItemMainCarouselBinding

class CarouselAdapter(private val navigateToGroupJoin: () -> Unit) : RecyclerView.Adapter<CarouselAdapter.ViewHolderClass>() {

    inner class ViewHolderClass(binding: ItemMainCarouselBinding) : RecyclerView.ViewHolder(binding.root) {
        var carouselImageView: ImageView = binding.carouselImageView
        init {
            carouselImageView.setOnClickListener { navigateToGroupJoin() }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderClass {
        val binding = ItemMainCarouselBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolderClass(binding)
    }

    override fun getItemCount(): Int = 10

    override fun onBindViewHolder(holder: ViewHolderClass, position: Int) {
        holder.carouselImageView.setImageResource(R.drawable.ic_launcher_logo)
    }
}

// TODO 실제 사진 파일을 가져온 이후에, data class 개편 후, Adapter와 Viewholder 분리 예정