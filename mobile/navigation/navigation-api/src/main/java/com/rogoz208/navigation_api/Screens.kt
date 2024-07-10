package com.rogoz208.navigation_api

import com.rogoz208.mobile_common.model.ArticleUI
import com.rogoz208.mobile_common.model.Filter
import com.rogoz208.mobile_common.model.SourceUI

sealed class Screens {

    data object HeadlinesScreen : Screens()
    data object SavedScreen : Screens()
    data object SourcesScreen : Screens()
    data class ArticleScreen(val article: ArticleUI) : Screens()
    data class ArticlesBySourceScreen(val source: SourceUI) : Screens()
    data class FiltersScreen(val resultKey: String, val filter: Filter) : Screens()
}