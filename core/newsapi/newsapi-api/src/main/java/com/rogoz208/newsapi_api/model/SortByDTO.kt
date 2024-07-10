package com.rogoz208.newsapi_api.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class SortByDTO {
    @SerialName("relevancy")
    relevancy,

    @SerialName("popularity")
    popularity,

    @SerialName("publishedAt")
    publishedAt
}