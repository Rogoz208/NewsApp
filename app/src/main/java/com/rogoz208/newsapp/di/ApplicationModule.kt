package com.rogoz208.newsapp.di

import android.content.Context
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
internal class ApplicationModule(private val context: Context) {

    @Singleton
    @Provides
    fun provideContext() = context
}