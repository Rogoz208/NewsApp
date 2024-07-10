package com.rogoz208.newsdata_api.model

import java.util.Date

data class Article(
    val cacheId: String = ID_NONE,
    val source: Source,
    val author: String?,
    val title: String?,
    val description: String?,
    val url: String,
    val urlToImage: String?,
    val publishedAt: Date,
    val content: String?,
    val saved: Boolean
) {

    companion object {
        const val ID_NONE = "ID_NONE"
    }
}

data class Source(
    val id: String?,
    val name: String?
)