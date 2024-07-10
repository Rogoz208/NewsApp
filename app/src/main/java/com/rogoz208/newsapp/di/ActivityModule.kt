package com.rogoz208.newsapp.di

import androidx.appcompat.app.AppCompatActivity
import com.github.terrakok.cicerone.androidx.AppNavigator
import com.rogoz208.newsapp.R
import dagger.Module
import dagger.Provides

@Module
internal class ActivityModule(private val activity: AppCompatActivity) {

    @Provides
    fun provideAppNavigator() = AppNavigator(activity, R.id.fragment_container)
}