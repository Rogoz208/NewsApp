package com.rogoz208.newsdata_api.di

import com.rogoz208.newsdata_api.ArticlesRepository

interface NewsDataProvider {
    val articlesRepository: ArticlesRepository
}