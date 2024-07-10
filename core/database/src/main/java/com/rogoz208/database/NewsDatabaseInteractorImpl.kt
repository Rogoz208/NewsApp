package com.rogoz208.database

import com.rogoz208.database.dao.ArticleDao
import com.rogoz208.database_api.model.ArticleDBO
import com.rogoz208.database_api.NewsDataBaseInteractor
import kotlinx.coroutines.flow.Flow

internal class NewsDatabaseInteractorImpl(
    private val articlesDao: ArticleDao
) : NewsDataBaseInteractor {

    override suspend fun getAll(): List<ArticleDBO> = articlesDao.getAll()

    override fun observeAll(): Flow<List<ArticleDBO>> = articlesDao.observeAll()
    override suspend fun getTopHeadlinesByCategory(category: String): List<ArticleDBO> =
        articlesDao.getTopHeadlinesByCategory(category)

    override fun observeTopHeadlinesByCategory(category: String): Flow<List<ArticleDBO>> =
        articlesDao.observeTopHeadlinesByCategory(category)

    override suspend fun getSavedArticles(): List<ArticleDBO> = articlesDao.getSavedArticles()

    override suspend fun insert(articles: List<ArticleDBO>) = articlesDao.insert(articles)

    override suspend fun saveArticle(article: ArticleDBO) = articlesDao.saveArticle(article)

    override suspend fun remove(id: String) = articlesDao.remove(id)

    override suspend fun clear() = articlesDao.clear()


}