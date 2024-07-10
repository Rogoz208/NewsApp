package com.rogoz208.navigation.di

import com.github.terrakok.cicerone.Cicerone
import com.github.terrakok.cicerone.Router
import com.rogoz208.navigation.RouterImpl
import com.rogoz208.navigation_api.IRouter
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
internal class NavigationModule {

    @Singleton
    @Provides
    fun provideRouter(): IRouter = RouterImpl()

    @Singleton
    @Provides
    fun provideCicerone(router: IRouter) = Cicerone.create(router as Router)

    @Singleton
    @Provides
    fun provideNavigatorHolder(cicerone: Cicerone<Router>) = cicerone.getNavigatorHolder()
}