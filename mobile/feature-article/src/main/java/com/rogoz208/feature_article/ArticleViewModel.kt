package com.rogoz208.feature_article

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rogoz208.feature_article.utils.toArticle
import com.rogoz208.mobile_common.model.ArticleUI
import com.rogoz208.navigation_api.IRouter
import com.rogoz208.newsdata_api.ArticlesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ArticleViewModel(
    private val router: IRouter, private val repository: ArticlesRepository
) : ViewModel() {

    private var articleUI: ArticleUI? = null

    private val _articleStateFlow = MutableStateFlow(ArticleUI())
    val articleStateFlow = _articleStateFlow.asStateFlow()

    private val _articleSavedStateFlow = MutableStateFlow(false)
    val articleSavedStateFlow = _articleSavedStateFlow.asStateFlow()

    fun onGettingArticle(article: ArticleUI) {
        _articleStateFlow.value = article
        articleUI = article
    }

    fun onMenuInflated() {
        _articleSavedStateFlow.value = articleUI?.saved ?: false
    }

    fun onArticleSaveClick() {
        viewModelScope.launch(Dispatchers.IO) {
            articleUI?.let {
                if (it.saved) {
                    repository.removeArticleFromDatabase(it.id)
                } else {
                    repository.saveArticleToDataBase(it.toArticle())
                }
                articleUI = articleUI!!.copy(saved = !it.saved)
            }
        }
        _articleSavedStateFlow.value = !_articleSavedStateFlow.value
    }

    fun onBackPressed() {
        router.back()
    }
}