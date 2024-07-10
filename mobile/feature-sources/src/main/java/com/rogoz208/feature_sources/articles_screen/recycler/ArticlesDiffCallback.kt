package com.rogoz208.feature_sources.articles_screen.recycler

import androidx.recyclerview.widget.DiffUtil
import com.rogoz208.mobile_common.model.ArticleUI


class ArticlesDiffCallback(
    private val oldList: List<ArticleUI>,
    private val newList: List<ArticleUI>
) : DiffUtil.Callback() {
    override fun getOldListSize() = oldList.size

    override fun getNewListSize() = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].id == newList[newItemPosition].id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].description == newList[newItemPosition].description
                && oldList[oldItemPosition].sourceName == newList[newItemPosition].sourceName
                && oldList[oldItemPosition].urlToImage == newList[newItemPosition].urlToImage
                && oldList[oldItemPosition].sourceLogoUrl == newList[newItemPosition].sourceLogoUrl
    }
}
