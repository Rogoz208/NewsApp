package com.rogoz208.feature_article_api

import com.rogoz208.navigation_api.IRouter
import com.rogoz208.newsdata_api.ArticlesRepository

interface FeatureArticleDependencies {
    val router: IRouter
    val repository: ArticlesRepository
}