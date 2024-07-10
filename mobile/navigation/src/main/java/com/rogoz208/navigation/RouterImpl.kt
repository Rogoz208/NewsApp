package com.rogoz208.navigation

import com.github.terrakok.cicerone.ResultListener
import com.github.terrakok.cicerone.Router
import com.github.terrakok.cicerone.androidx.FragmentScreen
import com.rogoz208.feature_article.createArticleScreen
import com.rogoz208.feature_filters.createFiltersScreen
import com.rogoz208.feature_headlines.presentation.headlines.HeadlinesFragment
import com.rogoz208.feature_saved.createSavedScreen
import com.rogoz208.feature_sources.articles_screen.createArticlesBySourceScreen
import com.rogoz208.feature_sources.sources_screen.createSourcesScreen
import com.rogoz208.mobile_common.model.ArticleUI
import com.rogoz208.mobile_common.model.Filter
import com.rogoz208.mobile_common.model.SourceUI
import com.rogoz208.navigation.Screens.articleScreen
import com.rogoz208.navigation.Screens.articlesBySource
import com.rogoz208.navigation.Screens.filtersScreen
import com.rogoz208.navigation.Screens.headlinesScreen
import com.rogoz208.navigation.Screens.savedScreen
import com.rogoz208.navigation.Screens.sourcesScreen
import com.rogoz208.navigation_api.IRouter
import com.rogoz208.navigation_api.Screens
import com.rogoz208.navigation_api.Screens.*

internal class RouterImpl : Router(), IRouter {
    override fun newRootScreen(screens: Screens) = newRootScreen(screens.convertScreensToScreen())

    override fun navigateToScreen(screens: Screens) = navigateTo(screens.convertScreensToScreen())

    override fun backTo(screens: Screens) = backTo(screens.convertScreensToScreen())

    override fun back() = exit()

    override fun setResult(key: String, data: Any) = sendResult(key, data)

    override fun setupResultListener(key: String, listener: ResultListener) {
        setResultListener(key, listener)
    }

    private fun Screens.convertScreensToScreen(): FragmentScreen {
        return when (this) {
            is HeadlinesScreen -> headlinesScreen()
            is SavedScreen -> savedScreen()
            is SourcesScreen -> sourcesScreen()
            is FiltersScreen -> filtersScreen(resultKey, filter)
            is ArticleScreen -> articleScreen(article)
            is ArticlesBySourceScreen -> articlesBySource(source)
        }
    }
}

internal object Screens {
    fun headlinesScreen() = FragmentScreen { HeadlinesFragment.newInstance() }

    fun savedScreen() = FragmentScreen { createSavedScreen() }

    fun sourcesScreen() = FragmentScreen { createSourcesScreen() }

    fun articleScreen(article: ArticleUI) = FragmentScreen { createArticleScreen(article) }

    fun articlesBySource(sourceUI: SourceUI) = FragmentScreen { createArticlesBySourceScreen(sourceUI) }

    fun filtersScreen(resultKey: String, filter: Filter) =
        FragmentScreen { createFiltersScreen(resultKey, filter) }
}
