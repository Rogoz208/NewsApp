package com.rogoz208.newsapi_api

import androidx.annotation.IntRange
import com.rogoz208.newsapi_api.model.ArticleDTO
import com.rogoz208.newsapi_api.model.CategoryDTO
import com.rogoz208.newsapi_api.model.LanguageDTO
import com.rogoz208.newsapi_api.model.ResponseDTO
import com.rogoz208.newsapi_api.model.ResponseSourceDTO
import com.rogoz208.newsapi_api.model.SortByDTO
import com.rogoz208.newsapi_api.model.SourceDetailedDTO

interface NewsApiInteractor {

    suspend fun getEverything(
        query: String? = null,
        from: String? = null,
        to: String? = null,
        language: LanguageDTO? = null,
        sortBy: SortByDTO? = null,
        sources: String? = null,
        @IntRange(from = 0, to = 100) pageSize: Int = 100,
        @IntRange(from = 1) page: Int = 1
    ): Result<ResponseDTO<ArticleDTO>>

    suspend fun getTopHeadlines(
        query: String? = null,
        category: CategoryDTO? = null,
        sources: String? = null,
        language: LanguageDTO? = null,
        @IntRange(from = 0, to = 100) pageSize: Int = 100,
        @IntRange(from = 1) page: Int = 1
    ): Result<ResponseDTO<ArticleDTO>>

    suspend fun getSources(): Result<ResponseSourceDTO<SourceDetailedDTO>>
}