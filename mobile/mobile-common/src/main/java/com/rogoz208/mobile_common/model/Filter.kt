package com.rogoz208.mobile_common.model

import android.os.Parcelable
import com.rogoz208.newsdata_api.model.Language
import com.rogoz208.newsdata_api.model.SortBy
import kotlinx.parcelize.Parcelize
import java.util.Date

@Parcelize
data class Filter(
    val sortBy: SortBy? = null,
    val language: Language? = null,
    val from: Date? = null,
    val to: Date? = null
) : Parcelable
