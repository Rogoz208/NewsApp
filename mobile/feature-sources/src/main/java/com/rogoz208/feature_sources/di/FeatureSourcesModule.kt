package com.rogoz208.feature_sources.di

import androidx.lifecycle.ViewModel
import com.rogoz208.feature_sources.articles_screen.ArticlesViewModel
import com.rogoz208.feature_sources.sources_screen.SourcesViewModel
import com.rogoz208.mobile_common.di.FragmentScope
import com.rogoz208.mobile_common.di.ViewModelKey
import com.rogoz208.navigation_api.IRouter
import com.rogoz208.newsdata_api.ArticlesRepository
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap

@Module
internal class FeatureSourcesModule {

    @ViewModelKey(SourcesViewModel::class)
    @IntoMap
    @FragmentScope
    @Provides
    fun provideSourcesViewModel(router: IRouter, repository: ArticlesRepository): ViewModel =
        SourcesViewModel(router, repository)

    @ViewModelKey(ArticlesViewModel::class)
    @IntoMap
    @FragmentScope
    @Provides
    fun provideArticlesViewModel(router: IRouter, repository: ArticlesRepository): ViewModel =
        ArticlesViewModel(router, repository)
}