package com.rogoz208.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.REPLACE
import androidx.room.Query
import com.rogoz208.database_api.model.ArticleDBO
import kotlinx.coroutines.flow.Flow

@Dao
interface ArticleDao {

    @Query("SELECT * FROM articles")
    suspend fun getAll(): List<ArticleDBO>

    @Query("SELECT * FROM articles")
    fun observeAll(): Flow<List<ArticleDBO>>

    @Query("SELECT * from articles WHERE category = :category")
    suspend fun getTopHeadlinesByCategory(category: String): List<ArticleDBO>

    @Query("SELECT * from articles WHERE category = :category")
    fun observeTopHeadlinesByCategory(category: String): Flow<List<ArticleDBO>>

    @Query("SELECT * FROM articles WHERE saved = 1")
    fun getSavedArticles(): List<ArticleDBO>

    @Query("SELECT * FROM articles WHERE saved = 1")
    fun getSavedArticlesWithFilter(): List<ArticleDBO>

    @Insert(onConflict = REPLACE)
    suspend fun insert(articles: List<ArticleDBO>)

    @Insert(onConflict = REPLACE)
    suspend fun saveArticle(article: ArticleDBO)

    @Query("DELETE FROM articles WHERE id = :id")
    suspend fun remove(id: String)

    @Query("DELETE FROM articles")
    suspend fun clear()
}