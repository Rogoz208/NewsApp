package com.rogoz208.feature_headlines.presentation.headlines;

import com.rogoz208.mobile_common.model.Filter;
import com.rogoz208.navigation_api.IRouter;
import com.rogoz208.navigation_api.Screens;

import moxy.InjectViewState;
import moxy.MvpPresenter;

@InjectViewState
public class HeadlinesPresenter extends MvpPresenter<HeadlinesView> {

    private static final String FILTERS_RESULT_KEY = "FILTERS_RESULT_KEY";
    private final IRouter router;

    private Filter filter = new Filter();

    public HeadlinesPresenter(IRouter router) {
        this.router = router;
    }

    public void onFiltersMenuItemClicked() {
        router.setupResultListener(FILTERS_RESULT_KEY, data -> {
            filter = (Filter) data;
            getViewState().setNewFilter(filter);
        });
        router.navigateToScreen(new Screens.FiltersScreen(FILTERS_RESULT_KEY, filter));
    }

    public void onPageChanged() {
        getViewState().setNewFilter(filter);
    }
}
