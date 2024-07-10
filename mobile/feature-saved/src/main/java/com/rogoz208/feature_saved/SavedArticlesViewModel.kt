package com.rogoz208.feature_saved

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rogoz208.mobile_common.utils.toArticleUI
import com.rogoz208.mobile_common.model.ArticleUI
import com.rogoz208.mobile_common.model.Filter
import com.rogoz208.navigation_api.IRouter
import com.rogoz208.navigation_api.Screens
import com.rogoz208.newsdata_api.ArticlesRepository
import com.rogoz208.newsdata_api.RequestResult
import com.rogoz208.newsdata_api.map
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.plus

class SavedArticlesViewModel(
    private val router: IRouter, private val repository: ArticlesRepository
) : ViewModel() {

    private val _savedArticlesStateFlow = MutableStateFlow<List<ArticleUI>>(listOf())
    val savedArticlesStateFlow = _savedArticlesStateFlow.asStateFlow()

    private val _isProgressStateFlow = MutableStateFlow(false)
    val isProgressStateFlow = _isProgressStateFlow.asStateFlow()

    private var filter: Filter = Filter()

    fun onScreenLoaded() {
        loadSavedArticles()
    }

    fun onFiltersMenuItemClicked() {
        router.setupResultListener(SAVED_RESULT_KEY) { data ->
            filter = data as Filter
        }
        router.navigateToScreen(Screens.FiltersScreen(SAVED_RESULT_KEY, filter))
    }

    fun onArticleItemClicked(item: ArticleUI, position: Int) {
        router.navigateToScreen(Screens.ArticleScreen(item))
    }

    private fun loadSavedArticles() {
        repository.loadSavedArticles().map { requestResult ->
            requestResult.map { articles -> articles.map { it.toArticleUI() } }
        }.onEach { requestResult: RequestResult<List<ArticleUI>> ->
            when (requestResult) {
                is RequestResult.Error -> {

                }

                is RequestResult.InProgress -> {
                    _isProgressStateFlow.value = true
                }

                is RequestResult.Success -> {
                    _isProgressStateFlow.value = false
                    _savedArticlesStateFlow.value = requestResult.data
                }
            }
        }.launchIn(viewModelScope + Dispatchers.IO)
    }

    private fun isFilterApplied(): Boolean {
        return filter.sortBy != null || filter.language != null || filter.from != null || filter.to != null
    }

    companion object {
        private const val SAVED_RESULT_KEY = "SAVED_RESULT_KEY"
    }
}