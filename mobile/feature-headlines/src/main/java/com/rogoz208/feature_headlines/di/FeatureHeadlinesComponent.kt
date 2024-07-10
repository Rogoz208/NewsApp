package com.rogoz208.feature_headlines.di

import com.rogoz208.feature_headlines.presentation.articles_list.ArticlesListFragment
import com.rogoz208.feature_headlines.presentation.headlines.HeadlinesFragment
import com.rogoz208.feature_headlines_api.FeatureHeadlinesDependencies
import com.rogoz208.mobile_common.di.FragmentScope
import dagger.Component
import me.vponomarenko.injectionmanager.x.XInjectionManager

@FragmentScope
@Component(
    modules = [FeatureHeadlinesModule::class],
    dependencies = [FeatureHeadlinesDependencies::class]
)
internal interface FeatureHeadlinesComponent {

    fun inject(fragment: HeadlinesFragment)
    fun inject(fragment: ArticlesListFragment)
}

internal fun buildFeatureHeadlinesComponent(): FeatureHeadlinesComponent {
    return DaggerFeatureHeadlinesComponent.builder()
        .featureHeadlinesDependencies(XInjectionManager.findComponent())
        .build();
}

internal fun findFeatureHeadlinesComponent(): FeatureHeadlinesComponent {
    return XInjectionManager.findComponent<FeatureHeadlinesComponent>()
}