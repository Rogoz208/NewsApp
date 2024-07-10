package com.rogoz208.feature_article.di

import androidx.lifecycle.ViewModel
import com.rogoz208.feature_article.ArticleViewModel
import com.rogoz208.mobile_common.di.FragmentScope
import com.rogoz208.mobile_common.di.ViewModelKey
import com.rogoz208.navigation_api.IRouter
import com.rogoz208.newsdata_api.ArticlesRepository
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap

@Module
internal class FeatureArticleModule {

    @ViewModelKey(ArticleViewModel::class)
    @IntoMap
    @FragmentScope
    @Provides
    fun provideArticleViewModel(router: IRouter, repository: ArticlesRepository): ViewModel =
        ArticleViewModel(router, repository)
}