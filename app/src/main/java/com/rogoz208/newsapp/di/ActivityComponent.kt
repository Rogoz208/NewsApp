package com.rogoz208.newsapp.di

import com.rogoz208.newsapp.MainActivity
import dagger.Subcomponent

@ActivityScope
@Subcomponent(modules = [ActivityModule::class])
internal interface ActivityComponent {

    fun inject(activity: MainActivity)
}