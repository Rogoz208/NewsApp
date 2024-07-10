package com.rogoz208.newsapp.di.aggregators

import com.rogoz208.database_api.di.NewsDatabaseProvider
import com.rogoz208.newsapi_api.di.NewsApiProvider
import com.rogoz208.newsdata_api.di.NewsDataDependencies
import dagger.Component

@Component(dependencies = [NewsDatabaseProvider::class, NewsApiProvider::class])
internal interface NewsDataDependenciesAggregator : NewsDataDependencies