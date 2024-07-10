package com.rogoz208.feature_headlines.presentation.articles_list;

import static com.rogoz208.feature_headlines.di.FeatureHeadlinesComponentKt.findFeatureHeadlinesComponent;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.rogoz208.feature_headlines.presentation.articles_list.recycler.ArticlesAdapter;
import com.rogoz208.feature_headlines.presentation.articles_list.recycler.OnArticleItemClickListener;
import com.rogoz208.feature_headlines.databinding.FragmentArticlesListBinding;
import com.rogoz208.feature_headlines.presentation.headlines.HeadlinesFragment;
import com.rogoz208.feature_headlines.presentation.headlines.TabItem;
import com.rogoz208.mobile_common.model.ArticleUI;
import com.rogoz208.mobile_common.model.Filter;
import com.rogoz208.newsdata_api.model.Category;

import java.util.List;

import javax.inject.Inject;

import moxy.MvpAppCompatFragment;
import moxy.presenter.InjectPresenter;
import moxy.presenter.ProvidePresenter;

public class ArticlesListFragment extends MvpAppCompatFragment implements ArticlesListView, HeadlinesFragment.OnSetNewFilter, HeadlinesFragment.OnSearchQuery {
    private final ArticlesAdapter adapter = new ArticlesAdapter();

    private FragmentArticlesListBinding binding;
    private Category category;
    private boolean isLoading = false;
    private boolean isLastPage = false;

    @Inject
    @InjectPresenter
    ArticlesListPresenter presenter;

    @ProvidePresenter
    ArticlesListPresenter providePresenter() {
        return presenter;
    }

    public static ArticlesListFragment newInstance(TabItem tabItem) {
        ArticlesListFragment fragment = new ArticlesListFragment();
        Bundle arguments = new Bundle();

        arguments.putParcelable("tabItem", tabItem);
        fragment.setArguments(arguments);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        findFeatureHeadlinesComponent().inject(this);
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentArticlesListBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        getCategoryFromArguments();

        initRecyclerView();
    }

    @Override
    public void onResume() {
        super.onResume();

        presenter.loadFirstPage(category);
        binding.progressBar.setVisibility(View.VISIBLE);
    }

    private void getCategoryFromArguments() {
        if (getArguments() != null) {
            TabItem tabItem = getArguments().getParcelable("tabItem");
            if (tabItem != null) {
                category = tabItem.getCategory();
            }
        }
    }

    private void initRecyclerView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(requireContext());
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(requireContext(),
                linearLayoutManager.getOrientation());
        adapter.setOnArticleItemClickListener(new OnArticleItemClickListener() {
            @Override
            public void onItemClick(ArticleUI item, int position) {
                presenter.onArticleItemClicked(item, position);
            }

            @Override
            public void onItemLongClick(ArticleUI item, View itemView, int position) {

            }
        });

        binding.recyclerView.setLayoutManager(linearLayoutManager);
        binding.recyclerView.addItemDecoration(dividerItemDecoration);
        binding.recyclerView.setAdapter(adapter);
        binding.recyclerView.addOnScrollListener(new ArticlesListScrollListener(linearLayoutManager) {
            @Override
            protected void loadMoreItems() {
                isLoading = true;
                adapter.addLoadingFooter();
                presenter.loadNextPage(category);
            }

            @Override
            public boolean isLastPage() {
                return isLastPage;
            }

            @Override
            public boolean isLoading() {
                return isLoading;
            }
        });

        binding.swipeContainer.setOnRefreshListener(() -> presenter.loadFirstPage(category));
    }

    @Override
    public void showFirstPage(List<ArticleUI> newArticles, boolean isLastPage) {
        this.isLastPage = isLastPage;
        binding.progressBar.setVisibility(View.INVISIBLE);
        binding.swipeContainer.setRefreshing(false);
        adapter.setNewData(newArticles);
    }

    @Override
    public void showNextPage(List<ArticleUI> newArticles, boolean isLastPage) {
        isLoading = false;
        this.isLastPage = isLastPage;
        adapter.removeLoadingFooter();
        adapter.setNewData(newArticles);
    }

    @Override
    public void showError(String errorMessage) {

    }

    @Override
    public void setNewFilterToFragment(Filter filter) {
        presenter.setNewFilter(filter);
    }

    @Override
    public void sendNewSearchQuery(String query) {
        presenter.search(query, category);
    }
}
