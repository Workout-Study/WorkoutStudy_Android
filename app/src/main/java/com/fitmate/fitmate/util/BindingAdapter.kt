package com.fitmate.fitmate.util

import android.content.Context
import android.net.Uri
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.cardview.widget.CardView
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.findViewTreeLifecycleOwner
import com.bumptech.glide.Glide
import com.facebook.shimmer.ShimmerFrameLayout
import com.fitmate.fitmate.R
import com.fitmate.fitmate.domain.model.FitProgressItem
import com.fitmate.fitmate.presentation.viewmodel.CertificateState
import java.net.URI

//인증화면 사진 설정하는 메서드(uri를 통한 바인딩이므로 잘 확인하고 사용해야함.)
@BindingAdapter("uri")
fun ImageView.setImageByUri(image: String) {
    Glide.with(this)
        .load(image.toUri())
        .into(this)
}

@BindingAdapter("url")
fun ImageView.setImageByUrl(imageUrl: String) {
    Glide.with(this)
        .load(imageUrl)
        .circleCrop()
        .into(this)
}

//인증 화면의 종료 사진 첨부 컨테이너 visible 설정
@BindingAdapter("certificate_end_image_container_state")
fun LinearLayout.setContainerVisible(state: CertificateState) {
    when (state) {
        CertificateState.NON_PROCEEDING, CertificateState.ADDED_START_IMAGE -> {
            this.visibility = View.GONE
        }

        CertificateState.PROCEEDING -> {
            this.visibility = View.VISIBLE
        }
    }
}

//인증 화면의 시작 사진 첨부 카드뷰 visible 설정
@BindingAdapter("certificate_end_image_add_state")
fun CardView.setContainerVisible(state: CertificateState) {
    when (state) {
        CertificateState.NON_PROCEEDING, CertificateState.ADDED_START_IMAGE -> {
            this.visibility = View.VISIBLE
        }

        CertificateState.PROCEEDING -> {
            this.visibility = View.GONE
        }
    }
}

//인증 화면의 버튼 상태 설정
@BindingAdapter("certificate_button_state")
fun Button.buttonSetState(state: CertificateState) {
    when (state) {
        CertificateState.NON_PROCEEDING -> {
            this.isEnabled = false
            this.text = this.context.getString(R.string.certificate_scr_confirm_unactive)
        }

        CertificateState.ADDED_START_IMAGE -> {
            this.isEnabled = true
            this.text = this.context.getString(R.string.certificate_scr_confirm)
        }

        CertificateState.PROCEEDING -> {
            this.isEnabled = true
            this.text = this.context.getString(R.string.certificate_scr_confirm_finish)
        }
    }
}

//인증 화면의 리셋 버튼 visible 설정
@BindingAdapter("certificate_reset_button_state")
fun Button.resetButtonSetState(state: CertificateState) {
    when (state) {
        CertificateState.NON_PROCEEDING -> {
            this.visibility = View.GONE
        }

        CertificateState.ADDED_START_IMAGE -> {
            this.visibility = View.GONE
        }

        CertificateState.PROCEEDING -> {
            this.visibility = View.VISIBLE
        }
    }
}

//my fit 화면의 아직 피트그룹에 가입되어있지 않은지 알려주는 컨테이너 visible 설정
@BindingAdapter("myfit_group_data_is_empty")
fun LinearLayout.setVisibleGuideFitGroupEnter(data: List<FitProgressItem>?) {
    if (data == null){
        this.visibility = View.GONE
    }else{
        if (data.isEmpty()) {
            this.visibility = View.VISIBLE
        } else {
            this.visibility = View.GONE
        }
    }
}
//my fit 화면의 Shimmer visible 설정
@BindingAdapter("myfit_group_data_shimmer")
fun ShimmerFrameLayout.setVisibleShimmer(data: List<FitProgressItem>?) {
    if (data == null){
        this.visibility = View.VISIBLE
    }else {
        startShimmer()
        this.visibility = View.GONE
    }
}





