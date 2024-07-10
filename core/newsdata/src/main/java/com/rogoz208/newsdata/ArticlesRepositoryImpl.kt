package com.rogoz208.newsdata

import android.util.Log
import com.rogoz208.database_api.NewsDataBaseInteractor
import com.rogoz208.database_api.model.ArticleDBO
import com.rogoz208.newsapi_api.NewsApiInteractor
import com.rogoz208.newsapi_api.model.ArticleDTO
import com.rogoz208.newsapi_api.model.ResponseDTO
import com.rogoz208.newsapi_api.model.ResponseSourceDTO
import com.rogoz208.newsapi_api.model.SourceDetailedDTO
import com.rogoz208.newsdata.utils.toArticle
import com.rogoz208.newsdata.utils.toArticleDbo
import com.rogoz208.newsdata.utils.toCategoryDTO
import com.rogoz208.newsdata.utils.toIso8601
import com.rogoz208.newsdata.utils.toLanguageDTO
import com.rogoz208.newsdata.utils.toSortByDTO
import com.rogoz208.newsdata.utils.toSourceDetailed
import com.rogoz208.newsdata_api.ArticlesRepository
import com.rogoz208.newsdata_api.RequestResult
import com.rogoz208.newsdata_api.map
import com.rogoz208.newsdata_api.model.Article
import com.rogoz208.newsdata_api.model.Category
import com.rogoz208.newsdata_api.model.Language
import com.rogoz208.newsdata_api.model.SortBy
import com.rogoz208.newsdata_api.model.SourceDetailed
import com.rogoz208.newsdata_api.utils.MergeStrategy
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.flow.onEach
import java.util.Date

class ArticlesRepositoryImpl(
    private val database: NewsDataBaseInteractor,
    private val api: NewsApiInteractor,
) : ArticlesRepository {

    override fun getTopHeadlines(
        query: String?,
        category: Category?,
        pageSize: Int,
        page: Int,
        mergeStrategy: MergeStrategy<RequestResult<List<Article>>>
    ): Flow<RequestResult<List<Article>>> {
        val cachedAllArticles: Flow<RequestResult<List<Article>>> =
            getTopHeadlinesFromDatabase(query, category, pageSize, page)

        val remoteArticles: Flow<RequestResult<List<Article>>> =
            getTopHeadlinesFromServer(query, category, pageSize, page)

        return cachedAllArticles.combine(remoteArticles) { left, right ->
            mergeStrategy.merge(left, right)
        }
    }

    override fun getSources(): Flow<RequestResult<List<SourceDetailed>>> {
        val apiRequest = flow {
            emit(api.getSources())
        }.map { result ->
            when {
                result.isSuccess -> RequestResult.Success(result.getOrThrow())

                result.isFailure -> RequestResult.Error(error = result.exceptionOrNull())
                else -> error("Impossible branch")
            }
        }.onEach { requestResult ->
            if (requestResult is RequestResult.Error) {
                Log.e(TAG, "Error getting data from server. Cause = ${requestResult.error}")
            }
        }

        val start =
            flowOf<RequestResult<ResponseSourceDTO<SourceDetailedDTO>>>(RequestResult.InProgress())

        return merge(apiRequest, start).map { requestResult ->
            requestResult.map { response ->
                response.sources.map { it.toSourceDetailed() }
            }
        }
    }

    override fun getAllArticles(
        query: String?,
        language: Language?,
        sortBy: SortBy?,
        source: String?,
        from: Date?,
        to: Date?,
        pageSize: Int,
        page: Int,
        mergeStrategy: MergeStrategy<RequestResult<List<Article>>>
    ): Flow<RequestResult<List<Article>>> {
        return getAllArticlesFromServer(query, from, to, language, sortBy, source, pageSize, page)
    }

    override suspend fun saveArticleToDataBase(article: Article) {
        database.saveArticle(article.toArticleDbo(true))
    }

    override suspend fun removeArticleFromDatabase(id: String) {
        database.remove(id)
    }

    override fun loadSavedArticles(): Flow<RequestResult<List<Article>>> {
        val dbRequest = flow {
            emit(database.getSavedArticles())
        }.map<List<ArticleDBO>, RequestResult<List<ArticleDBO>>> {
            RequestResult.Success(it)
        }.catch {
            Log.e(TAG, "Error getting data from database. Cause = $it")
            emit(RequestResult.Error(error = it))
        }

        val start = flowOf<RequestResult<List<ArticleDBO>>>(RequestResult.InProgress())

        return merge(start, dbRequest).map { result ->
            result.map { articlesDbos ->
                articlesDbos.map { it.toArticle() }
            }
        }
    }

    private fun getTopHeadlinesFromDatabase(
        query: String?, category: Category?, pageSize: Int, page: Int
    ): Flow<RequestResult<List<Article>>> {
        val dbRequest = flow {
            emit(database.getTopHeadlinesByCategory(category.toString()))
        }.map<List<ArticleDBO>, RequestResult<List<ArticleDBO>>> {
            RequestResult.Success(it)
        }.catch {
            Log.e(TAG, "Error getting data from database. Cause = $it")
            emit(RequestResult.Error(error = it))
        }

        val start = flowOf<RequestResult<List<ArticleDBO>>>(RequestResult.InProgress())

        return merge(start, dbRequest).map { result ->
            result.map { articlesDbos ->
                articlesDbos.map { it.toArticle() }
            }
        }
    }

    private fun getTopHeadlinesFromServer(
        query: String?, category: Category?, pageSize: Int, page: Int
    ): Flow<RequestResult<List<Article>>> {
        val apiRequest = flow {
            emit(
                api.getTopHeadlines(
                    query = query,
                    category = category?.toCategoryDTO(),
                    pageSize = pageSize,
                    page = page
                )
            )
        }.map { result ->
            when {
                result.isSuccess -> RequestResult.Success(
                    result.getOrThrow(),
                    result.getOrThrow().totalResults
                )

                result.isFailure -> RequestResult.Error(error = result.exceptionOrNull())
                else -> error("Impossible branch")
            }
        }.onEach { requestResult ->
            if (requestResult is RequestResult.Error) {
                Log.e(TAG, "Error getting data from server. Cause = ${requestResult.error}")
            }
        }.map { requestResult ->
            requestResult.map { responseDTO ->
                val filteredArticles =
                    responseDTO.articles.filter { articleDTO -> articleDTO.title != "[Removed]" }
                ResponseDTO(responseDTO.status, responseDTO.totalResults, filteredArticles)
            }
        }.onEach { requestResult ->
            if (requestResult is RequestResult.Success) {
                saveArticlesToCache(requestResult.data.articles, category)
            }
        }

        val start = flowOf<RequestResult<ResponseDTO<ArticleDTO>>>(RequestResult.InProgress())
        return merge(apiRequest, start).map { requestResult ->
            requestResult.map { response ->
                response.articles.map { it.toArticle() }
            }
        }
    }

    private fun getAllArticlesFromServer(
        query: String?,
        from: Date?,
        to: Date?,
        language: Language?,
        sortBy: SortBy?,
        source: String?,
        pageSize: Int,
        page: Int
    ): Flow<RequestResult<List<Article>>> {
        val apiRequest = flow {
            emit(
                api.getEverything(
                    query = query,
                    from = from?.toIso8601(),
                    to = to?.toIso8601(),
                    language = language?.toLanguageDTO(),
                    sortBy = sortBy?.toSortByDTO(),
                    sources = source,
                    pageSize = pageSize,
                    page = page
                )
            )
        }.map { result ->
            when {
                result.isSuccess -> RequestResult.Success(
                    result.getOrThrow(),
                    result.getOrThrow().totalResults
                )

                result.isFailure -> RequestResult.Error(error = result.exceptionOrNull())
                else -> error("Impossible branch")
            }
        }.onEach { requestResult ->
            if (requestResult is RequestResult.Error) {
                Log.e(TAG, "Error getting data from server. Cause = ${requestResult.error}")
            }
        }.map { requestResult ->
            requestResult.map { responseDTO ->
                val filteredArticles =
                    responseDTO.articles.filter { articleDTO -> articleDTO.title != "[Removed]" }
                ResponseDTO(responseDTO.status, responseDTO.totalResults, filteredArticles)
            }
        }

        val start = flowOf<RequestResult<ResponseDTO<ArticleDTO>>>(RequestResult.InProgress())
        return merge(apiRequest, start).map { requestResult ->
            requestResult.map { response ->
                response.articles.map { it.toArticle() }
            }
        }
    }

    private suspend fun saveArticlesToCache(data: List<ArticleDTO>, category: Category?) {
        val articlesDbos = data.map { articleDto -> articleDto.toArticleDbo(category) }
        database.insert(articlesDbos)
    }

    private companion object {
        const val TAG = "ArticlesRepositoryImpl"
    }
}