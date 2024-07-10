package com.rogoz208.feature_headlines.presentation.articles_list;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.rogoz208.feature_headlines.presentation.headlines.TabItem;

import java.util.List;

public class ArticlesListFragmentStateAdapter extends FragmentStateAdapter {

    private final List<TabItem> tabItems;

    public ArticlesListFragmentStateAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle, List<TabItem> tabItems) {
        super(fragmentManager, lifecycle);
        this.tabItems = tabItems;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return ArticlesListFragment.newInstance(tabItems.get(position));
    }

    @Override
    public int getItemCount() {
        return tabItems.size();
    }
}
