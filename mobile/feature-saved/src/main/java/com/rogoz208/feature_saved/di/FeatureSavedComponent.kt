package com.rogoz208.feature_saved.di

import com.rogoz208.feature_saved.SavedFragment
import com.rogoz208.feature_saved_api.FeatureSavedDependencies
import com.rogoz208.mobile_common.di.FragmentScope
import dagger.Component

@FragmentScope
@Component(
    modules = [FeatureSavedModule::class],
    dependencies = [FeatureSavedDependencies::class]
)
internal interface FeatureSavedComponent {

    fun inject(fragment: SavedFragment)
}