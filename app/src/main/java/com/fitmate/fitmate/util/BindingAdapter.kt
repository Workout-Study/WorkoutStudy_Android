package com.fitmate.fitmate.util

import android.content.Context
import android.net.Uri
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.cardview.widget.CardView
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.fitmate.fitmate.R
import com.fitmate.fitmate.presentation.viewmodel.CertificateState
import java.net.URI

@BindingAdapter("uri")
fun ImageView.setImage(image: String) {
    Glide.with(this)
        .load(image.toUri())
        .into(this)
}

@BindingAdapter("certificate_end_image_container_state")
fun LinearLayout.setContainerVisible(state:CertificateState){
    when(state){
        CertificateState.NON_PROCEEDING,CertificateState.ADDED_START_IMAGE ->{
            this.visibility = View.GONE
        }
        CertificateState.PROCEEDING ->{
            this.visibility = View.VISIBLE
        }
    }
}

@BindingAdapter("certificate_end_image_add_state")
fun CardView.setContainerVisible(state:CertificateState){
    when(state){
        CertificateState.NON_PROCEEDING,CertificateState.ADDED_START_IMAGE ->{
            this.visibility = View.VISIBLE
        }
        CertificateState.PROCEEDING ->{
            this.visibility = View.GONE
        }
    }
}

@BindingAdapter("certificate_button_state")
fun Button.buttonSetState(state:CertificateState){
    when(state){
        CertificateState.NON_PROCEEDING ->{
            this.isEnabled = false
            this.text = this.context.getString(R.string.certificate_scr_confirm_unactive)
        }
        CertificateState.ADDED_START_IMAGE ->{
            this.isEnabled = true
            this.text = this.context.getString(R.string.certificate_scr_confirm)
        }
        CertificateState.PROCEEDING ->{
            this.isEnabled = true
            this.text = this.context.getString(R.string.certificate_scr_confirm_finish)
        }
    }
}
