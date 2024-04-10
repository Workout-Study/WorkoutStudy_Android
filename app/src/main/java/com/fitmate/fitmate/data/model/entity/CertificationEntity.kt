package com.fitmate.fitmate.data.model.entity

import android.net.Uri
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable
import java.time.Instant

@Entity("Certification")
data class CertificationEntity(
    @PrimaryKey(true)
    var id: Int? = null,
    @ColumnInfo
    val userId: String,
    @ColumnInfo
    val recordStartDate: Instant,
    @ColumnInfo
    var recordEndDate: Instant? = null,
    @ColumnInfo
    val startImages: MutableList<Uri>,
    @ColumnInfo
    var endImages: MutableList<Uri>,
    @ColumnInfo
    var certificateTime: Long? = null,
    @ColumnInfo
    var startImagesUrl: MutableList<String>,
    @ColumnInfo
    var endImagesUrl: MutableList<String>
) : Serializable
