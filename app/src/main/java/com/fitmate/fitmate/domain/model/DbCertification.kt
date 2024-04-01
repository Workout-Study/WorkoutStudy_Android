package com.fitmate.fitmate.domain.model

import android.net.Uri
import java.io.Serializable
import java.net.URI
import java.time.Instant

data class DbCertification (
    val id: Int? = null,
    val userId: String,
    val recordStartDate: Instant,
    val recordEndDate: Instant? = null,
    val startImages:MutableList<Uri>,
    val endImages:MutableList<Uri>? = null,
    val certificateTime:Long? = null
): Serializable