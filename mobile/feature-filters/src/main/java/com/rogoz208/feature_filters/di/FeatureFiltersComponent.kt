package com.rogoz208.feature_filters.di

import com.rogoz208.feature_filters.FiltersFragment
import com.rogoz208.feature_filters_api.FeatureFiltersDependencies
import com.rogoz208.mobile_common.di.FragmentScope
import dagger.Component

@FragmentScope
@Component(
    modules = [FeatureFiltersModule::class],
    dependencies = [FeatureFiltersDependencies::class]
)
interface FeatureFiltersComponent {

    fun inject(fragment: FiltersFragment)
}