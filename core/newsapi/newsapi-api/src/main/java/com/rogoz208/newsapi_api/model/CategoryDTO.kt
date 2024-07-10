package com.rogoz208.newsapi_api.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class CategoryDTO {

    @SerialName("business")
    BUSINESS,

    @SerialName("entertainment")
    ENTERTAINMENT,

    @SerialName("general")
    GENERAL,

    @SerialName("health")
    HEALTH,

    @SerialName("science")
    SCIENCE,

    @SerialName("sports")
    SPORTS,

    @SerialName("technology")
    TECHNOLOGY
}