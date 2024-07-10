package com.rogoz208.database_api

import com.rogoz208.database_api.model.ArticleDBO
import kotlinx.coroutines.flow.Flow

interface NewsDataBaseInteractor {

    suspend fun getAll(): List<ArticleDBO>

    fun observeAll(): Flow<List<ArticleDBO>>

    suspend fun getTopHeadlinesByCategory(category: String): List<ArticleDBO>

    fun observeTopHeadlinesByCategory(category: String): Flow<List<ArticleDBO>>

    suspend fun getSavedArticles(): List<ArticleDBO>

    suspend fun insert(articles: List<ArticleDBO>)

    suspend fun saveArticle(article: ArticleDBO)

    suspend fun remove(id: String)

    suspend fun clear()
}
