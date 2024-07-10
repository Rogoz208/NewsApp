package com.rogoz208.newsapp.di

import com.rogoz208.database_api.di.NewsDatabaseDependencies
import com.rogoz208.database_api.di.NewsDatabaseProvider
import com.rogoz208.feature_article_api.FeatureArticleDependencies
import com.rogoz208.feature_filters_api.FeatureFiltersDependencies
import com.rogoz208.feature_headlines_api.FeatureHeadlinesDependencies
import com.rogoz208.feature_saved_api.FeatureSavedDependencies
import com.rogoz208.feature_sources_api.FeatureSourcesDependencies
import com.rogoz208.navigation_api.di.NavigationProvider
import com.rogoz208.newsapi_api.di.NewsApiProvider
import com.rogoz208.newsdata_api.di.NewsDataDependencies
import com.rogoz208.newsdata_api.di.NewsDataProvider
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [ApplicationModule::class],
    dependencies = [NavigationProvider::class, NewsDatabaseProvider::class, NewsApiProvider::class, NewsDataProvider::class]
)
internal interface ApplicationComponent : ApplicationComponentProvider,
    FeatureHeadlinesDependencies, FeatureSavedDependencies, FeatureSourcesDependencies,
    FeatureArticleDependencies,
    FeatureFiltersDependencies, NewsDatabaseDependencies, NewsDataDependencies {

    fun plus(activityModule: ActivityModule): ActivityComponent

    @Component.Builder
    interface Builder {
        fun setApplicationModule(module: ApplicationModule): Builder

        fun setNavigationProvider(navigationProvider: NavigationProvider): Builder

        fun setNewsDatabaseProvider(newsDatabaseProvider: NewsDatabaseProvider): Builder

        fun setNewsApiProvider(newsApiProvider: NewsApiProvider): Builder

        fun setNewsDataProvider(newsDataProvider: NewsDataProvider): Builder

        fun build(): ApplicationComponent
    }
}