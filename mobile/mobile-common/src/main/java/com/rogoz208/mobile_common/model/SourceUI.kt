package com.rogoz208.mobile_common.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class SourceUI(
    val id: String = "",
    val name: String = "",
    val category: String = "",
    val country: String = "",
    val urlToImage: String? = "",
) : Parcelable