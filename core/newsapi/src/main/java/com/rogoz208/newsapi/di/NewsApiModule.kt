package com.rogoz208.newsapi.di

import com.rogoz208.newsapi.BuildConfig
import com.rogoz208.newsapi.NewsApi
import com.rogoz208.newsapi_api.NewsApiInteractor
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import javax.inject.Singleton

@Module
internal class NewsApiModule {

    @Singleton
    @Provides
    fun provideHttpClient(): OkHttpClient? = if (BuildConfig.DEBUG) {
        val logging = HttpLoggingInterceptor()
            .setLevel(HttpLoggingInterceptor.Level.BODY)
        OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()
    } else {
        null
    }

    @Singleton
    @Provides
    fun provideNewsApiInteractor(okHttpClient: OkHttpClient?): NewsApiInteractor = NewsApi(
        baseUrl = BuildConfig.NEWS_API_BASE_URL,
        apiKey = BuildConfig.NEWS_API_KEY,
        okHttpClient = okHttpClient
    )
}