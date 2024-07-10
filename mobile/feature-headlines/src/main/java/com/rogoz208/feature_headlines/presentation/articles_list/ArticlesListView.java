package com.rogoz208.feature_headlines.presentation.articles_list;

import com.rogoz208.mobile_common.model.ArticleUI;

import java.util.List;

import moxy.MvpView;
import moxy.viewstate.strategy.alias.AddToEndSingle;
import moxy.viewstate.strategy.alias.OneExecution;

public interface ArticlesListView extends MvpView {

    @AddToEndSingle
    void showFirstPage(List<ArticleUI> articles, boolean isLastPage);

    @AddToEndSingle
    void showNextPage(List<ArticleUI> articles, boolean isLastPage);

    @OneExecution
    void showError(String errorMessage);
}
