package com.rogoz208.feature_sources.sources_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rogoz208.mobile_common.model.Filter
import com.rogoz208.mobile_common.model.SourceUI
import com.rogoz208.mobile_common.utils.toSourceUI
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

class SourcesViewModel(
    private val router: IRouter,
    private val repository: ArticlesRepository
) : ViewModel() {

    private val _sourcesStateFlow = MutableStateFlow<List<SourceUI>>(listOf())
    val sourcesStateFlow = _sourcesStateFlow.asStateFlow()

    private val _isProgressStateFlow = MutableStateFlow(false)
    val isProgressStateFlow = _isProgressStateFlow.asStateFlow()

    private var filter: Filter = Filter()

    fun onScreenLoaded() {
        loadSources()
    }

    fun onFiltersMenuItemClicked() {
        router.setupResultListener(SOURCES_RESULT_KEY) { data ->
            filter = data as Filter
        }
        router.navigateToScreen(Screens.FiltersScreen(SOURCES_RESULT_KEY, filter))
    }

    fun onSourceItemClicked(item: SourceUI, position: Int) {
        router.navigateToScreen(Screens.ArticlesBySourceScreen(item))
    }

    private fun loadSources() {
        repository.getSources().map { requestResult ->
            requestResult.map { articles -> articles.map { it.toSourceUI() } }
        }.onEach { requestResult: RequestResult<List<SourceUI>> ->
            when (requestResult) {
                is RequestResult.Error -> {

                }

                is RequestResult.InProgress -> {
                    _isProgressStateFlow.value = true
                }

                is RequestResult.Success -> {
                    _isProgressStateFlow.value = false
                    _sourcesStateFlow.value = requestResult.data
                }
            }
        }.launchIn(viewModelScope + Dispatchers.IO)
    }

    companion object {
        private const val SOURCES_RESULT_KEY = "SOURCES_RESULT_KEY"
    }
}