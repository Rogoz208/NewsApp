package com.rogoz208.feature_headlines_api

import com.rogoz208.navigation_api.IRouter
import com.rogoz208.newsdata_api.ArticlesRepository

interface FeatureHeadlinesDependencies {
    val router: IRouter
    val articlesRepository: ArticlesRepository
}