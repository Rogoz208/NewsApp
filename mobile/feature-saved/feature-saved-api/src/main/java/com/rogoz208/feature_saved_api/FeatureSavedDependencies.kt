package com.rogoz208.feature_saved_api

import com.rogoz208.navigation_api.IRouter
import com.rogoz208.newsdata_api.ArticlesRepository

interface FeatureSavedDependencies {
    val router: IRouter
    val repository: ArticlesRepository
}