package com.rogoz208.newsapi_api.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class LanguageDTO {
    @SerialName("de")
    de,

    @SerialName("en")
    en,

    @SerialName("ru")
    ru
}