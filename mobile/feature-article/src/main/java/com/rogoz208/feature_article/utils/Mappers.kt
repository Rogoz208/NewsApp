package com.rogoz208.feature_article.utils

import com.rogoz208.mobile_common.model.ArticleUI
import com.rogoz208.newsdata_api.model.Article
import com.rogoz208.newsdata_api.model.Source

fun ArticleUI.toArticle(): Article {
    return Article(
        cacheId = id + "saved",
        source = Source(null, sourceName),
        author = null,
        title = title,
        description = description,
        url = url,
        urlToImage = urlToImage,
        publishedAt = publishedAt,
        content = content,
        saved = saved
    )
}