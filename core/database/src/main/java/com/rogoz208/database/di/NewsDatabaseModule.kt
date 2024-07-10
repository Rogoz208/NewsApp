package com.rogoz208.database.di

import android.content.Context
import com.rogoz208.database.NewsDatabase
import com.rogoz208.database.NewsDatabaseInteractorImpl
import com.rogoz208.database_api.NewsDataBaseInteractor
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class NewsDatabaseModule(private val context: Context) {

    @Singleton
    @Provides
    fun provideNewsDatabase(): NewsDatabase = NewsDatabase(context)

    @Singleton
    @Provides
    fun provideNewsDatabaseInteractor(database: NewsDatabase): NewsDataBaseInteractor =
        NewsDatabaseInteractorImpl(database.articlesDao())
}