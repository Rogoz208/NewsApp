package com.rogoz208.feature_headlines.presentation.headlines;

import static com.rogoz208.feature_headlines.di.FeatureHeadlinesComponentKt.buildFeatureHeadlinesComponent;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuProvider;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayoutMediator;
import com.rogoz208.feature_headlines.R;
import com.rogoz208.feature_headlines.presentation.articles_list.ArticlesListFragment;
import com.rogoz208.feature_headlines.presentation.articles_list.ArticlesListFragmentStateAdapter;
import com.rogoz208.feature_headlines.databinding.FragmentHeadlinesBinding;
import com.rogoz208.feature_headlines.di.FeatureHeadlinesComponent;
import com.rogoz208.mobile_common.model.Filter;
import com.rogoz208.newsdata_api.model.Category;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import me.vponomarenko.injectionmanager.IHasComponent;
import me.vponomarenko.injectionmanager.x.XInjectionManager;
import moxy.MvpAppCompatFragment;
import moxy.presenter.InjectPresenter;
import moxy.presenter.ProvidePresenter;

public class HeadlinesFragment extends MvpAppCompatFragment implements HeadlinesView, MenuProvider, IHasComponent<FeatureHeadlinesComponent> {

    public static HeadlinesFragment newInstance() {
        return new HeadlinesFragment();
    }

    @Inject
    @InjectPresenter
    HeadlinesPresenter presenter;

    @ProvidePresenter
    HeadlinesPresenter providePresenter() {
        return presenter;
    }

    private final List<TabItem> tabItems = new ArrayList<>(
            Arrays.asList(
                    new TabItem(R.string.headlines_tab_menu_general, R.drawable.ic_general_24dp, Category.GENERAL),
                    new TabItem(R.string.headlines_tab_menu_business, R.drawable.ic_business_24dp, Category.BUSINESS),
                    new TabItem(R.string.headlines_tab_menu_science, R.drawable.ic_science_24dp, Category.SCIENCE),
                    new TabItem(R.string.headlines_tab_menu_health, R.drawable.ic_health_24dp, Category.HEALTH),
                    new TabItem(R.string.headlines_tab_menu_entertaiment, R.drawable.ic_entertaiment_24dp, Category.ENTERTAIMENT),
                    new TabItem(R.string.headlines_tab_menu_technology, R.drawable.ic_technology_24dp, Category.TECHNOLOGY),
                    new TabItem(R.string.headlines_tab_menu_sport, R.drawable.ic_sport_24dp, Category.SPORTS)
            )
    );
    private FragmentHeadlinesBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        XInjectionManager.bindComponent(this).inject(this);
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentHeadlinesBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        AppCompatActivity activity = (AppCompatActivity) requireActivity();
        activity.setSupportActionBar(binding.toolbar);
        activity.addMenuProvider(this, getViewLifecycleOwner(), Lifecycle.State.RESUMED);
        initTabLayout();
    }

    @Override
    public FeatureHeadlinesComponent getComponent() {
        return buildFeatureHeadlinesComponent();
    }

    @NonNull
    @Override
    public String getComponentKey() {
        return buildFeatureHeadlinesComponent().toString();
    }

    @Override
    public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
        menuInflater.inflate(R.menu.headlines_toolbar_menu, menu);
    }

    @Override
    public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {

        int menuItemId = menuItem.getItemId();
        if (menuItemId == R.id.toolbar_menu_item_search) {
            binding.searchView.getEditText().addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    // Do nothing
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    ArticlesListFragment fragment = (ArticlesListFragment) getChildFragmentManager().findFragmentByTag("f" + binding.viewPager.getCurrentItem());
                    if (fragment != null) {
                        fragment.sendNewSearchQuery(s.toString());
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {
                    // Do nothing
                }
            });
            binding.searchView.show();
        }

        if (menuItemId == R.id.toolbar_menu_item_filter) {
            presenter.onFiltersMenuItemClicked();
            return true;
        }
        return false;
    }

    @Override
    public void setNewFilter(Filter filter) {
        if (filter.getSortBy() != null
                || filter.getLanguage() != null
                || filter.getFrom() != null
                || filter.getTo() != null) {
            binding.tabLayout.setVisibility(View.GONE);
            binding.viewPager.setUserInputEnabled(false);
        }
        ArticlesListFragment fragment = (ArticlesListFragment) getChildFragmentManager().findFragmentByTag("f" + binding.viewPager.getCurrentItem());
        if (fragment != null) {
            fragment.setNewFilterToFragment(filter);
        }
    }

    private void initTabLayout() {
        binding.viewPager.setAdapter(new ArticlesListFragmentStateAdapter(getChildFragmentManager(), getLifecycle(), tabItems));
        binding.viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                presenter.onPageChanged();
            }
        });

        new TabLayoutMediator(binding.tabLayout, binding.viewPager, ((tab, position) -> {
            String tabTitle = getString(tabItems.get(position).getNameResourceId());

            tab.setText(tabTitle);
            tab.setIcon(tabItems.get(position).getIconResourceId());
        })).attach();

    }

    public interface OnSetNewFilter {
        void setNewFilterToFragment(Filter filter);
    }

    public interface OnSearchQuery {
        void sendNewSearchQuery(String query);
    }
}
