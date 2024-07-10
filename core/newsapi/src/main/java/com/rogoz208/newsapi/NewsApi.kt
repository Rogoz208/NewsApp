package com.rogoz208.newsapi

import androidx.annotation.IntRange
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.rogoz208.newsapi.utils.NewsApiKeyInterceptor
import com.rogoz208.newsapi_api.NewsApiInteractor
import com.rogoz208.newsapi_api.model.ArticleDTO
import com.rogoz208.newsapi_api.model.CategoryDTO
import com.rogoz208.newsapi_api.model.LanguageDTO
import com.rogoz208.newsapi_api.model.ResponseDTO
import com.rogoz208.newsapi_api.model.ResponseSourceDTO
import com.rogoz208.newsapi_api.model.SortByDTO
import com.rogoz208.newsapi_api.model.SourceDetailedDTO
import com.skydoves.retrofit.adapters.result.ResultCallAdapterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.create
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * [API Documentation](https://newsapi.org/docs/get-started)
 */
internal interface NewsApi : NewsApiInteractor {

    /**
     * API details [here](https://newsapi.org/docs/endpoints/everything)
     */
    @GET("everything")
    override suspend fun getEverything(
        @Query("q") query: String?,
        @Query("from") from: String?,
        @Query("to") to: String?,
        @Query("language") language: LanguageDTO?,
        @Query("sortBy") sortBy: SortByDTO?,
        @Query("sources") sources: String?,
        @Query("pageSize") @IntRange(from = 0, to = 100) pageSize: Int,
        @Query("page") @IntRange(from = 1) page: Int
    ): Result<ResponseDTO<ArticleDTO>>


    /**
     * API details [here](https://newsapi.org/docs/endpoints/top-headlines)
     */
    @GET("top-headlines")
    override suspend fun getTopHeadlines(
        @Query("q") query: String?,
        @Query("category") category: CategoryDTO?,
        @Query("sources") sources: String?,
        @Query("language") language: LanguageDTO?,
        @Query("pageSize") @IntRange(from = 0, to = 100) pageSize: Int,
        @Query("page") @IntRange(from = 1) page: Int
    ): Result<ResponseDTO<ArticleDTO>>

    /**
     * API details [here](https://newsapi.org/docs/endpoints/sources)
     */
    @GET("top-headlines/sources")
    override suspend fun getSources(): Result<ResponseSourceDTO<SourceDetailedDTO>>
}

internal fun NewsApi(
    baseUrl: String,
    apiKey: String,
    okHttpClient: OkHttpClient? = null,
    json: Json = Json
): NewsApi = retrofit(baseUrl, apiKey, okHttpClient, json).create()

internal fun retrofit(
    baseUrl: String,
    apiKey: String,
    okHttpClient: OkHttpClient?,
    json: Json
): Retrofit {
    val jsonConverterFactory = json.asConverterFactory("application/json".toMediaType())

    val modifiedOkHttpClient = (okHttpClient?.newBuilder() ?: OkHttpClient.Builder())
        .addInterceptor(NewsApiKeyInterceptor(apiKey))
        .build()

    return Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(jsonConverterFactory)
        .addCallAdapterFactory(ResultCallAdapterFactory.create())
        .client(modifiedOkHttpClient)
        .build()
}
