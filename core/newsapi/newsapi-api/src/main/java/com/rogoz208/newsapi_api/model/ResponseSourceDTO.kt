package com.rogoz208.newsapi_api.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ResponseSourceDTO<E>(
    @SerialName("status") val status: String,
    @SerialName("sources") val sources: List<E>
)