package com.rogoz208.feature_headlines.presentation.headlines

import android.os.Parcelable
import com.rogoz208.newsdata_api.model.Category
import kotlinx.parcelize.Parcelize

@Parcelize
data class TabItem(val nameResourceId: Int, val iconResourceId: Int, val category: Category) : Parcelable