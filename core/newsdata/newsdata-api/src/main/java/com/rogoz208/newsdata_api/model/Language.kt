package com.rogoz208.newsdata_api.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class Language {
    @SerialName("de")
    DE,

    @SerialName("en")
    EN,

    @SerialName("ru")
    RU
}