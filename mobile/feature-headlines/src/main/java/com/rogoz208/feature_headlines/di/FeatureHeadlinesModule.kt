package com.rogoz208.feature_headlines.di

import com.rogoz208.feature_headlines.presentation.articles_list.ArticlesListPresenter
import com.rogoz208.feature_headlines.model.GetAllArticlesUseCase
import com.rogoz208.feature_headlines.model.GetTopHeadlinesUseCase
import com.rogoz208.feature_headlines.presentation.headlines.HeadlinesPresenter
import com.rogoz208.mobile_common.di.FragmentScope
import com.rogoz208.navigation_api.IRouter
import com.rogoz208.newsdata_api.ArticlesRepository
import dagger.Module
import dagger.Provides

@Module
internal class FeatureHeadlinesModule {

    @FragmentScope
    @Provides
    fun provideHeadlinesPresenter(router: IRouter) =
        HeadlinesPresenter(router)

    @FragmentScope
    @Provides
    fun provideArticlesListPresenter(repository: ArticlesRepository, router: IRouter) =
        ArticlesListPresenter(
            GetTopHeadlinesUseCase(repository),
            GetAllArticlesUseCase(repository),
            router
        )
}