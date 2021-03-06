package com.team214.nctue4.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import kotlinx.serialization.Serializable

@Serializable
@Parcelize
data class FileItem(
    val name: String,
    val url: String
) : Parcelable
