package com.rogoz208.feature_headlines.presentation.articles_list.recycler;

import android.view.View;

import com.rogoz208.mobile_common.model.ArticleUI;

public interface OnArticleItemClickListener {

    void onItemClick(ArticleUI item, int position);

    void onItemLongClick(ArticleUI item, View itemView, int position);
}
