package com.rogoz208.feature_headlines.presentation.articles_list.recycler;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;


import com.rogoz208.mobile_common.model.ArticleUI;

import java.util.ArrayList;
import java.util.List;

public class ArticlesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int LOADING = 0;
    private static final int ITEM = 1;

    private final List<ArticleUI> articles = new ArrayList<>();

    private boolean isLoadingAdded = false;

    private OnArticleItemClickListener clickListener = null;

    public void setNewData(List<ArticleUI> newData) {
        ArticlesDiffCallback articlesDiffCallback = new ArticlesDiffCallback(articles, newData);
        DiffUtil.DiffResult result = DiffUtil.calculateDiff(articlesDiffCallback);
        articles.clear();
        articles.addAll(newData);
        result.dispatchUpdatesTo(this);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;

        switch (viewType) {
            case ITEM:
                viewHolder = new ArticleViewHolder(parent, clickListener);
                break;
            case LOADING:
                viewHolder = new LoadingViewHolder(parent);
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ArticleUI article = articles.get(position);

        switch (getItemViewType(position)) {
            case ITEM:
                ArticleViewHolder articleViewHolder = (ArticleViewHolder) holder;
                articleViewHolder.bind(article);
                break;
            case LOADING:
                LoadingViewHolder loadingViewHolder = (LoadingViewHolder) holder;
                loadingViewHolder.setProgressBarVisibility(View.VISIBLE);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return articles.size();
    }

    @Override
    public int getItemViewType(int position) {
        return (position == articles.size() - 1 && isLoadingAdded) ? LOADING : ITEM;
    }

    public void addLoadingFooter() {
        isLoadingAdded = true;
    }

    public void removeLoadingFooter() {
        isLoadingAdded = false;
    }

    private ArticleUI getItem(int position) {
        return articles.get(position);
    }

    public void setOnArticleItemClickListener(OnArticleItemClickListener listener) {
        clickListener = listener;
    }
}
