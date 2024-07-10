package com.rogoz208.feature_sources.sources_screen.recycler

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.rogoz208.mobile_common.model.SourceUI

class SourceAdapter : RecyclerView.Adapter<SourceViewHolder>() {

    private val sources: MutableList<SourceUI> = ArrayList()

    private var clickListener: OnSourceItemClickListener? = null

    fun setNewData(newData: List<SourceUI>) {
        val savedArticlesDiffCallback = SourceDiffCallback(sources, newData)
        val result = DiffUtil.calculateDiff(savedArticlesDiffCallback)
        sources.clear()
        sources.addAll(newData)
        result.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SourceViewHolder {
        return SourceViewHolder(parent, clickListener!!)
    }

    override fun onBindViewHolder(holder: SourceViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    override fun getItemCount() = sources.size

    private fun getItem(position: Int) = sources[position]

    fun setOnItemClickListener(listener: OnSourceItemClickListener) {
        clickListener = listener
    }
}