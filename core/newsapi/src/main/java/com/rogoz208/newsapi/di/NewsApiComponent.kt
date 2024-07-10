package com.rogoz208.newsapi.di

import com.rogoz208.newsapi_api.di.NewsApiProvider
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [NewsApiModule::class])
interface NewsApiComponent: NewsApiProvider