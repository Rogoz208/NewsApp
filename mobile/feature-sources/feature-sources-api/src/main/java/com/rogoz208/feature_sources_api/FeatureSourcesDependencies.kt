package com.rogoz208.feature_sources_api

import com.rogoz208.navigation_api.IRouter
import com.rogoz208.newsdata_api.ArticlesRepository

interface FeatureSourcesDependencies {
    val router: IRouter
    val repository: ArticlesRepository
}