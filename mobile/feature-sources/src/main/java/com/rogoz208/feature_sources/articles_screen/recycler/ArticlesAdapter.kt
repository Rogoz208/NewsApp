package com.rogoz208.feature_sources.articles_screen.recycler

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.rogoz208.mobile_common.model.ArticleUI

class ArticlesAdapter : RecyclerView.Adapter<ArticleViewHolder>() {

    private val articles: MutableList<ArticleUI> = ArrayList()

    private var clickListener: OnArticleItemClickListener? = null

    fun setNewData(newData: List<ArticleUI>) {
        val articlesDiffCallback = ArticlesDiffCallback(articles, newData)
        val result = DiffUtil.calculateDiff(articlesDiffCallback)
        articles.clear()
        articles.addAll(newData)
        result.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
        return ArticleViewHolder(parent, clickListener!!)
    }

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    override fun getItemCount() = articles.size

    private fun getItem(position: Int) = articles[position]

    fun setOnItemClickListener(listener: OnArticleItemClickListener) {
        clickListener = listener
    }
}