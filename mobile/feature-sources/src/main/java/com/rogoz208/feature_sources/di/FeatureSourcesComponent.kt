package com.rogoz208.feature_sources.di

import com.rogoz208.feature_sources.articles_screen.ArticlesFragment
import com.rogoz208.feature_sources.sources_screen.SourcesFragment
import com.rogoz208.feature_sources_api.FeatureSourcesDependencies
import com.rogoz208.mobile_common.di.FragmentScope
import dagger.Component

@FragmentScope
@Component(
    modules = [FeatureSourcesModule::class],
    dependencies = [FeatureSourcesDependencies::class]
)
internal interface FeatureSourcesComponent {

    fun inject(fragment: SourcesFragment)
    fun inject(fragment: ArticlesFragment)
}