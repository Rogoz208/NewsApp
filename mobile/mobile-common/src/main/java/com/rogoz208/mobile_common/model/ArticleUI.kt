package com.rogoz208.mobile_common.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.Date

@Parcelize
data class ArticleUI(
    val id: String = "",
    val title: String = "",
    val description: String = "",
    val sourceName: String = "",
    val sourceLogoUrl: String? = "",
    val url: String = "",
    val urlToImage: String? = "",
    val publishedAt: Date = Date(),
    val content: String? = "",
    val saved: Boolean = false
) : Parcelable