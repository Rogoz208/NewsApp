package com.rogoz208.feature_sources.sources_screen.recycler

import androidx.recyclerview.widget.DiffUtil
import com.rogoz208.mobile_common.model.SourceUI

class SourceDiffCallback(
    private val oldList: List<SourceUI>,
    private val newList: List<SourceUI>
) : DiffUtil.Callback() {

    override fun getOldListSize() = oldList.size

    override fun getNewListSize() = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].id == newList[newItemPosition].id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].name == newList[newItemPosition].name
                && oldList[oldItemPosition].category == newList[newItemPosition].category
                && oldList[oldItemPosition].country == newList[newItemPosition].country
    }
}