package com.rogoz208.newsdata_api.di

import com.rogoz208.database_api.NewsDataBaseInteractor
import com.rogoz208.newsapi_api.NewsApiInteractor

interface NewsDataDependencies {
    val database: NewsDataBaseInteractor
    val api: NewsApiInteractor
}