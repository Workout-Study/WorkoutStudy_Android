package com.fitmate.fitmate.data.model.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable
import java.net.URI
import java.time.Instant
import java.util.Date

@Entity("Certification")
data class CertificationEntity(
    @PrimaryKey(false)
    var id: Int,
    @ColumnInfo
    val userId: String,
    @ColumnInfo
    val recordStartDate: Instant,
    @ColumnInfo
    var recordEndDate: Instant,
    @ColumnInfo
    val startImages:MutableList<URI>,
    @ColumnInfo
    var endImages:MutableList<URI>
): Serializable
