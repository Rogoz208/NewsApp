package com.rogoz208.newsdata.utils

import com.rogoz208.database_api.model.ArticleDBO
import com.rogoz208.database_api.model.SourceDBO
import com.rogoz208.newsapi_api.model.ArticleDTO
import com.rogoz208.newsapi_api.model.CategoryDTO
import com.rogoz208.newsapi_api.model.LanguageDTO
import com.rogoz208.newsapi_api.model.SortByDTO
import com.rogoz208.newsapi_api.model.SourceDTO
import com.rogoz208.newsapi_api.model.SourceDetailedDTO
import com.rogoz208.newsdata_api.model.Article
import com.rogoz208.newsdata_api.model.Category
import com.rogoz208.newsdata_api.model.Language
import com.rogoz208.newsdata_api.model.SortBy
import com.rogoz208.newsdata_api.model.Source
import com.rogoz208.newsdata_api.model.SourceDetailed
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

internal fun ArticleDBO.toArticle(): Article = Article(
    cacheId = id,
    source = sourceDBO.toSource(),
    author = author,
    title = title,
    description = description,
    url = url,
    urlToImage = urlToImage,
    publishedAt = publishedAt,
    content = content,
    saved = saved
)

internal fun SourceDetailedDTO.toSourceDetailed(): SourceDetailed = SourceDetailed(
    id = id,
    name = name,
    category = category,
    country = country,
    url = url
)

internal fun SourceDBO.toSource(): Source = Source(id = id, name = name)

internal fun SourceDTO.toSource(author: String?): Source = Source(id = id, name = name ?: author)

internal fun SourceDTO.toSourceDBO(author: String?): SourceDBO =
    SourceDBO(id = id, name = name ?: author)

internal fun ArticleDTO.toArticle(): Article =
    Article(
        cacheId = url + publishedAt,
        source = source.toSource(author),
        author = author,
        title = title,
        description = description ?: title,
        url = url,
        urlToImage = urlToImage,
        publishedAt = publishedAt,
        content = content,
        saved = false
    )

internal fun ArticleDTO.toArticleDbo(category: Category? = null): ArticleDBO = ArticleDBO(
    id = url + publishedAt,
    sourceDBO = source.toSourceDBO(author),
    author = author,
    title = title,
    description = description ?: title,
    url = url,
    urlToImage = urlToImage,
    publishedAt = publishedAt,
    content = content,
    category = category.toString()
)

internal fun Article.toArticleDbo(saved: Boolean? = null): ArticleDBO = ArticleDBO(
    id = cacheId,
    sourceDBO = SourceDBO(source.id, source.name),
    author = author,
    title = title,
    description = description,
    url = url,
    urlToImage = urlToImage,
    publishedAt = publishedAt,
    content = content,
    category = null,
    saved = saved ?: this.saved
)

internal fun Category.toCategoryDTO(): CategoryDTO = when (this) {
    Category.ENTERTAIMENT -> CategoryDTO.ENTERTAINMENT
    Category.BUSINESS -> CategoryDTO.BUSINESS
    Category.GENERAL -> CategoryDTO.GENERAL
    Category.HEALTH -> CategoryDTO.HEALTH
    Category.SCIENCE -> CategoryDTO.SCIENCE
    Category.SPORTS -> CategoryDTO.SPORTS
    Category.TECHNOLOGY -> CategoryDTO.TECHNOLOGY
}

internal fun Language.toLanguageDTO(): LanguageDTO = when (this) {
    Language.DE -> LanguageDTO.de
    Language.EN -> LanguageDTO.en
    Language.RU -> LanguageDTO.ru
}

internal fun SortBy.toSortByDTO(): SortByDTO = when (this) {
    SortBy.RELEVANCY -> SortByDTO.relevancy
    SortBy.POPULARITY -> SortByDTO.popularity
    SortBy.PUBLISHED_AT -> SortByDTO.publishedAt
}

internal fun Date.toIso8601(): String {
    return SimpleDateFormat("yyyy-MM-DD", Locale.US).format(this)
}