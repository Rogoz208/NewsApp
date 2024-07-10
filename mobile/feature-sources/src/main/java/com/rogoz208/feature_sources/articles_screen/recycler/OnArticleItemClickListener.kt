package com.rogoz208.feature_sources.articles_screen.recycler

import android.view.View
import com.rogoz208.mobile_common.model.ArticleUI

interface OnArticleItemClickListener {

    fun onArticleClick(item: ArticleUI, position: Int)

    fun onArticleLongClick(item: ArticleUI, itemView: View, position: Int)
}