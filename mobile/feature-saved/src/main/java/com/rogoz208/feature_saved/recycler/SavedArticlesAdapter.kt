package com.rogoz208.feature_saved.recycler

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.rogoz208.mobile_common.model.ArticleUI

class SavedArticlesAdapter : RecyclerView.Adapter<SavedArticleViewHolder>() {

    private val articles: MutableList<ArticleUI> = ArrayList()

    private var clickListener: OnSavedArticleItemClickListener? = null

    fun setNewData(newData: List<ArticleUI>) {
        val savedArticlesDiffCallback = SavedArticlesDiffCallback(articles, newData)
        val result = DiffUtil.calculateDiff(savedArticlesDiffCallback)
        articles.clear()
        articles.addAll(newData)
        result.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SavedArticleViewHolder {
        return SavedArticleViewHolder(parent, clickListener!!)
    }

    override fun onBindViewHolder(holder: SavedArticleViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    override fun getItemCount() = articles.size

    private fun getItem(position: Int) = articles[position]

    fun setOnItemClickListener(listener: OnSavedArticleItemClickListener) {
        clickListener = listener
    }
}