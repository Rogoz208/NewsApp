package com.rogoz208.feature_headlines.presentation.headlines;

import com.rogoz208.mobile_common.model.Filter;

import moxy.MvpView;
import moxy.viewstate.strategy.alias.AddToEndSingle;

public interface HeadlinesView extends MvpView {

    @AddToEndSingle
    void setNewFilter(Filter filter);

}
