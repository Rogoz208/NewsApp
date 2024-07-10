package com.rogoz208.newsdata_api

import com.rogoz208.newsdata_api.model.Article
import com.rogoz208.newsdata_api.model.Category
import com.rogoz208.newsdata_api.model.Language
import com.rogoz208.newsdata_api.model.SortBy
import com.rogoz208.newsdata_api.model.SourceDetailed
import com.rogoz208.newsdata_api.utils.MergeStrategy
import com.rogoz208.newsdata_api.utils.RequestResponseMergeStrategy
import kotlinx.coroutines.flow.Flow
import java.util.Date

interface ArticlesRepository {

    fun getTopHeadlines(
        query: String? = null,
        category: Category? = null,
        pageSize: Int = 20,
        page: Int = 1,
        mergeStrategy: MergeStrategy<RequestResult<List<Article>>> = RequestResponseMergeStrategy()
    ): Flow<RequestResult<List<Article>>>

    fun getAllArticles(
        query: String? = null,
        language: Language? = null,
        sortBy: SortBy? = null,
        source: String? = null,
        from: Date? = null,
        to: Date? = null,
        pageSize: Int = 20,
        page: Int = 1,
        mergeStrategy: MergeStrategy<RequestResult<List<Article>>> = RequestResponseMergeStrategy()
    ): Flow<RequestResult<List<Article>>>

    suspend fun saveArticleToDataBase(article: Article)

    suspend fun removeArticleFromDatabase(id: String)

    fun loadSavedArticles(): Flow<RequestResult<List<Article>>>
    fun getSources(): Flow<RequestResult<List<SourceDetailed>>>
}