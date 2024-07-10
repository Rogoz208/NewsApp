package com.rogoz208.mobile_common.utils

import android.util.Log
import com.rogoz208.mobile_common.model.ArticleUI
import com.rogoz208.mobile_common.model.SourceUI
import com.rogoz208.newsdata_api.model.Article
import com.rogoz208.newsdata_api.model.SourceDetailed
import java.net.MalformedURLException
import java.net.URL

private const val TAG = "UI_Mappers"

fun Article.toArticleUI() = ArticleUI(
    id = cacheId,
    title = title ?: "",
    description = description ?: "",
    sourceName = source.name ?: "",
    sourceLogoUrl = getSourceLogoUrl(url),
    url = url,
    urlToImage = urlToImage,
    publishedAt = publishedAt,
    content = content,
    saved = saved
)

fun SourceDetailed.toSourceUI() = SourceUI(
        id = id,
        name = name,
        category = category,
        country = country,
        urlToImage = getSourceLogoUrl(url)
    )

private fun getSourceLogoUrl(url: String): String? {
    val articleUrl: URL?
    var sourceLogoUrl: String? = null
    try {
        articleUrl = URL(url)
        sourceLogoUrl = "${articleUrl.protocol}://${articleUrl.host}/favicon.ico"
    } catch (e: MalformedURLException) {
        Log.d(TAG, "toArticleUI: ${e.message}")
    }
    return sourceLogoUrl
}