package com.fitmate.fitmate.domain.model

import android.net.Uri
import androidx.room.ColumnInfo
import java.io.Serializable
import java.time.Instant

data class DbCertification(
    val id: Int? = null,
    val userId: String,
    val recordStartDate: Instant,
    val recordEndDate: Instant? = null,
    val startImages: MutableList<Uri>,
    val endImages: MutableList<Uri>? = null,
    val certificateTime: Long? = null,
    var startImagesUrl: MutableList<String>? = null,
    var endImagesUrl: MutableList<String>? = null
) : Serializable