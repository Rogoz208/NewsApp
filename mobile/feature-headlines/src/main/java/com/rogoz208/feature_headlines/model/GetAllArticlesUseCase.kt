package com.rogoz208.feature_headlines.model

import com.rogoz208.mobile_common.utils.toArticleUI
import com.rogoz208.mobile_common.model.ArticleUI
import com.rogoz208.newsdata_api.ArticlesRepository
import com.rogoz208.newsdata_api.RequestResult
import com.rogoz208.newsdata_api.map
import com.rogoz208.newsdata_api.model.Language
import com.rogoz208.newsdata_api.model.SortBy
import io.reactivex.rxjava3.core.Observable
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.rx3.asObservable
import java.util.Date

class GetAllArticlesUseCase(private val repository: ArticlesRepository) {

    operator fun invoke(
        query: String?,
        language: Language?,
        sortBy: SortBy?,
        from: Date?,
        to: Date?,
        pageSize: Int,
        page: Int,
    ): Observable<RequestResult<List<ArticleUI>>> {
        return repository.getAllArticles(query, language, sortBy, source = null, from, to, pageSize, page)
            .map { requestResult -> requestResult.map { articles -> articles.map { it.toArticleUI() } } }
            .asObservable()
    }
}