package com.rogoz208.feature_saved.di

import androidx.lifecycle.ViewModel
import com.rogoz208.feature_saved.SavedArticlesViewModel
import com.rogoz208.mobile_common.di.FragmentScope
import com.rogoz208.mobile_common.di.ViewModelKey
import com.rogoz208.navigation_api.IRouter
import com.rogoz208.newsdata_api.ArticlesRepository
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap

@Module
internal class FeatureSavedModule {

    @ViewModelKey(SavedArticlesViewModel::class)
    @IntoMap
    @FragmentScope
    @Provides
    fun provideSavedViewModel(router: IRouter, repository: ArticlesRepository): ViewModel =
        SavedArticlesViewModel(router, repository)
}