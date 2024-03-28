package com.fitmate.fitmate.domain.model

import java.net.URI
import java.time.Instant

data class DbCertification (
    val id: Int? = null,
    val userId: String,
    val recordStartDate: Instant,
    val recordEndDate: Instant? = null,
    val startImages:MutableList<URI>,
    val endImages:MutableList<URI>? = null
)