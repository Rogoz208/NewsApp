package com.rogoz208.navigation.di

import com.rogoz208.navigation_api.di.NavigationProvider
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [NavigationModule::class])
interface NavigationComponent : NavigationProvider