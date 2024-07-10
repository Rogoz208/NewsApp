package com.rogoz208.feature_headlines.presentation.articles_list;

import android.util.Log;

import com.rogoz208.feature_headlines.model.GetAllArticlesUseCase;
import com.rogoz208.feature_headlines.model.GetTopHeadlinesUseCase;
import com.rogoz208.mobile_common.model.ArticleUI;
import com.rogoz208.mobile_common.model.Filter;
import com.rogoz208.navigation_api.IRouter;
import com.rogoz208.navigation_api.Screens;
import com.rogoz208.newsdata_api.RequestResult;
import com.rogoz208.newsdata_api.model.Category;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import moxy.InjectViewState;
import moxy.MvpPresenter;

@InjectViewState
public class ArticlesListPresenter extends MvpPresenter<ArticlesListView> {

    private static final String TAG = "ArticlesListPresenter";
    private static final int PAGE_SIZE = 20;
    private static final int PAGE_START = 1;

    private final GetTopHeadlinesUseCase getTopHeadlinesUseCase;
    private final GetAllArticlesUseCase getAllArticlesUseCase;
    private final IRouter router;
    private final List<ArticleUI> articles = new ArrayList<>();

    private int currentPage = PAGE_START;
    private int totalPages = PAGE_START;
    private boolean isLastPage = false;
    private String query = null;
    private Filter filter = new Filter();

    public ArticlesListPresenter(
            GetTopHeadlinesUseCase getTopHeadlinesUseCase,
            GetAllArticlesUseCase getAllArticlesUseCase,
            IRouter router
    ) {
        this.getTopHeadlinesUseCase = getTopHeadlinesUseCase;
        this.getAllArticlesUseCase = getAllArticlesUseCase;
        this.router = router;
    }

    public void loadFirstPage(Category category) {
        resetPage();

        if (isFilterApplied()) {
            loadFirstPageWithFilter();
        } else {
            loadFirstPageWithoutFilter(category);
        }

    }

    public void loadNextPage(Category category) {
        currentPage++;
        if (isFilterApplied()) {
            loadNextPageWithFilter();
        } else {
            loadNextPageWithoutFilter(category);
        }

    }

    public void onArticleItemClicked(ArticleUI article, int position) {
        router.navigateToScreen(new Screens.ArticleScreen(article));
    }

    public void setNewFilter(Filter filter) {
        this.filter = filter;
    }

    public void search(String query, Category category) {
        this.query = query;
        loadFirstPage(category);
    }

    private void showFirstPage(List<ArticleUI> articles, int totalResults) {
        totalPages = getTotalPages(totalResults);
        isLastPage = currentPage == totalPages;

        this.articles.clear();
        this.articles.addAll(articles);

        getViewState().showFirstPage(this.articles, isLastPage);
    }

    private void showNextPage(List<ArticleUI> articles) {
        this.articles.addAll(articles);
        isLastPage = currentPage == totalPages;
        getViewState().showNextPage(this.articles, isLastPage);
    }

    private void showError(Throwable error) {
        String errorMessage;

        if (error != null) {
            errorMessage = error.getMessage();
        } else {
            errorMessage = "Unknown error";
        }

        getViewState().showError(errorMessage);
    }

    private int getTotalPages(int totalResults) {
        if (totalResults % PAGE_SIZE == 0) {
            return totalResults / PAGE_SIZE;
        } else {
            return totalResults / PAGE_SIZE + 1;
        }
    }

    private void resetPage() {
        currentPage = PAGE_START;
        totalPages = PAGE_START;
        isLastPage = false;

        articles.clear();

        getViewState().showFirstPage(articles, isLastPage);
    }

    private void loadFirstPageWithFilter() {
        getAllArticlesUseCase.invoke(
                "window",
                filter.getLanguage(),
                filter.getSortBy(),
                filter.getFrom(),
                filter.getTo(),
                PAGE_SIZE,
                currentPage
        ).observeOn(AndroidSchedulers.mainThread()).doOnNext(requestResult -> {
            if (requestResult instanceof RequestResult.Success) {
                showFirstPage(requestResult.getData(), requestResult.getTotalResults());
            }
            if (requestResult instanceof RequestResult.Error) {
                showError(((RequestResult.Error<List<ArticleUI>>) requestResult).getError());
                if (requestResult.getData() != null) {
                    showFirstPage(requestResult.getData(), requestResult.getTotalResults());
                }
            }
        }).doOnError(error -> {
            Log.e(TAG, "Error loading first page. Cause = " + error);
            showError(error);
        }).subscribe();
    }

    private void loadFirstPageWithoutFilter(Category category) {
        getTopHeadlinesUseCase.invoke(
                query,
                category,
                PAGE_SIZE,
                currentPage
        ).observeOn(AndroidSchedulers.mainThread()).doOnNext(requestResult -> {
            if (requestResult instanceof RequestResult.Success) {
                showFirstPage(requestResult.getData(), requestResult.getTotalResults());
            }
            if (requestResult instanceof RequestResult.Error) {
                showError(((RequestResult.Error<List<ArticleUI>>) requestResult).getError());
                if (requestResult.getData() != null) {
                    showFirstPage(requestResult.getData(), requestResult.getTotalResults());
                }
            }
        }).doOnError(error -> {
            Log.e(TAG, "Error loading first page. Cause = " + error);
            showError(error);
        }).subscribe();
    }

    private void loadNextPageWithFilter() {
        getAllArticlesUseCase.invoke(
                "window",
                filter.getLanguage(),
                filter.getSortBy(),
                filter.getFrom(),
                filter.getTo(),
                PAGE_SIZE,
                currentPage
        ).observeOn(AndroidSchedulers.mainThread()).doOnNext(requestResult -> {
            if (requestResult instanceof RequestResult.Success) {
                showNextPage(requestResult.getData());
            }
            if (requestResult instanceof RequestResult.Error) {
                showError(((RequestResult.Error<List<ArticleUI>>) requestResult).getError());
            }
        }).doOnError(error -> {
            Log.e(TAG, "Error loading next page. Cause = " + error);
            showError(error);
        }).subscribe();
    }

    private void loadNextPageWithoutFilter(Category category) {
        getTopHeadlinesUseCase.invoke(
                query,
                category,
                PAGE_SIZE,
                currentPage
        ).observeOn(AndroidSchedulers.mainThread()).doOnNext(requestResult -> {
            if (requestResult instanceof RequestResult.Success) {
                showNextPage(requestResult.getData());
            }
            if (requestResult instanceof RequestResult.Error) {
                showError(((RequestResult.Error<List<ArticleUI>>) requestResult).getError());
            }
        }).doOnError(error -> {
            Log.e(TAG, "Error loading next page. Cause = " + error);
            showError(error);
        }).subscribe();
    }

    private boolean isFilterApplied() {
        return filter.getSortBy() != null || filter.getLanguage() != null || filter.getFrom() != null || filter.getTo() != null;
    }
}
