package com.rogoz208.newsapi_api.di

import com.rogoz208.newsapi_api.NewsApiInteractor

interface NewsApiProvider {
    val newsApiInteractor: NewsApiInteractor
}