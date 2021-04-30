package com.garciaph.flickergallery.domain.entities

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Photo (
    val id: Long = -1L,
    val title: String = "",
    val owner: String = "",
    val thumbnailUrl: String = "",
    val fullscreenUrl: String = ""
) : Parcelable
