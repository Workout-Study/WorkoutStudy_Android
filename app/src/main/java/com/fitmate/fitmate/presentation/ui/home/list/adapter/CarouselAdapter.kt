package com.fitmate.fitmate.presentation.ui.home.list.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.fitmate.fitmate.R
import com.fitmate.fitmate.databinding.ItemMainCarouselBinding

class CarouselAdapter(private val imageUrls: List<String>, private val navigateToGroupJoin: () -> Unit) : RecyclerView.Adapter<CarouselAdapter.ViewHolderClass>() {

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

    override fun getItemCount(): Int = 5

    override fun onBindViewHolder(holder: ViewHolderClass, position: Int) {
        // 임의의 URL에서 이미지를 로드하여 설정
        Glide.with(holder.carouselImageView)
            .load(imageUrls[position]) // position에 해당하는 URL에서 이미지 로드
            .placeholder(R.drawable.add_picture) // 이미지 로드 전 placeholder 설정
            .error(R.drawable.ic_launcher_logo) // 이미지 로드 에러 시 error image 설정
            .centerCrop() // 이미지를 가로로 길게 자르기
            .into(holder.carouselImageView) // ImageView에 이미지 설정
    }
}

// TODO 실제 사진 파일을 가져온 이후에, data class 개편 후, Adapter와 Viewholder 분리 예정