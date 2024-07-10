package com.rogoz208.navigation_api.di

import com.github.terrakok.cicerone.NavigatorHolder
import com.rogoz208.navigation_api.IRouter

interface NavigationProvider {
    val router: IRouter
    val navigatorHolder: NavigatorHolder
}