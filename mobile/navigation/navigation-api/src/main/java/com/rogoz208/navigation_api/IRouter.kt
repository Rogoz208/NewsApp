package com.rogoz208.navigation_api

import com.github.terrakok.cicerone.ResultListener

interface IRouter {

    fun newRootScreen(screens: Screens)

    fun navigateToScreen(screens: Screens)

    fun backTo(screens: Screens)

    fun back()

    fun setResult(key: String, data: Any)

    fun setupResultListener(key: String, listener: ResultListener)
}