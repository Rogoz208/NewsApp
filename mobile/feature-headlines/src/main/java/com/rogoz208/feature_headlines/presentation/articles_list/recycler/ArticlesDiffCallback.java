package com.rogoz208.feature_headlines.presentation.articles_list.recycler;

import androidx.recyclerview.widget.DiffUtil;


import com.rogoz208.mobile_common.model.ArticleUI;

import java.util.List;
import java.util.Objects;

public class ArticlesDiffCallback extends DiffUtil.Callback {

    private final List<ArticleUI> oldList;
    private final List<ArticleUI> newList;

    public ArticlesDiffCallback(List<ArticleUI> oldList, List<ArticleUI> newList) {
        this.oldList = oldList;
        this.newList = newList;
    }

    @Override
    public int getOldListSize() {
        return oldList.size();
    }

    @Override
    public int getNewListSize() {
        return newList.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return Objects.equals(oldList.get(oldItemPosition).getId(), newList.get(newItemPosition).getId());
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        return Objects.equals(oldList.get(oldItemPosition).getDescription(), newList.get(newItemPosition).getDescription())
                && Objects.equals(oldList.get(oldItemPosition).getSourceName(), newList.get(newItemPosition).getSourceName())
                && Objects.equals(oldList.get(oldItemPosition).getUrlToImage(), newList.get(newItemPosition).getUrlToImage())
                && Objects.equals(oldList.get(oldItemPosition).getSourceLogoUrl(), newList.get(newItemPosition).getSourceLogoUrl());
    }
}
