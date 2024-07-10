package com.rogoz208.feature_filters.di

import androidx.lifecycle.ViewModel
import com.rogoz208.feature_filters.FiltersViewModel
import com.rogoz208.mobile_common.di.FragmentScope
import com.rogoz208.mobile_common.di.ViewModelKey
import com.rogoz208.navigation_api.IRouter
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap

@Module
internal class FeatureFiltersModule {

    @ViewModelKey(FiltersViewModel::class)
    @IntoMap
    @FragmentScope
    @Provides
    fun provideFiltersViewModel(router: IRouter): ViewModel = FiltersViewModel(router)
}