package com.fitmate.fitmate.presentation.viewmodel
import android.net.Uri
import android.os.Parcelable
import java.io.Serializable


data class GalleryImage(
    var uri: Uri = Uri.EMPTY,
    var name: String = "",
    var bucketId: Long = 0,
    var bucketName: String = "",
    var addedDate: Long = 0,
    var isSelected: Boolean = false,
) : Serializable