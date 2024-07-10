package com.rogoz208.newsdata.di

import com.rogoz208.database_api.NewsDataBaseInteractor
import com.rogoz208.newsapi_api.NewsApiInteractor
import com.rogoz208.newsdata.ArticlesRepositoryImpl
import com.rogoz208.newsdata_api.ArticlesRepository
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
internal class NewsDataModule {

    @Singleton
    @Provides
    fun provideArticlesRepository(
        database: NewsDataBaseInteractor,
        api: NewsApiInteractor
    ): ArticlesRepository = ArticlesRepositoryImpl(database, api)
}