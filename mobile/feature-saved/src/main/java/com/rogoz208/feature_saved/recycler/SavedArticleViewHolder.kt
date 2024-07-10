package com.rogoz208.feature_saved.recycler

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bumptech.glide.Glide
import com.rogoz208.feature_saved.R
import com.rogoz208.feature_saved.databinding.SavedArticleViewHolderBinding
import com.rogoz208.mobile_common.model.ArticleUI

class SavedArticleViewHolder(
    parent: ViewGroup, private val clickListener: OnSavedArticleItemClickListener
) : RecyclerView.ViewHolder(
    LayoutInflater.from(parent.context).inflate(R.layout.saved_article_view_holder, parent, false)
) {

    private val binding by viewBinding(SavedArticleViewHolderBinding::bind)

    fun bind(article: ArticleUI) {
        Glide.with(itemView).load(article.urlToImage)
            .placeholder(com.rogoz208.mobile_common.R.drawable.image_placeholder)
            .into(binding.articleImageView)

        Glide.with(itemView).load(article.sourceLogoUrl)
            .placeholder(com.rogoz208.mobile_common.R.drawable.image_placeholder).circleCrop()
            .into(binding.sourceImageView)

        binding.articleDescriptionTextView.text = article.description
        binding.sourceTitleTextView.text = article.sourceName

        itemView.setOnClickListener {
            clickListener.onArticleClick(article, this.layoutPosition)
        }
        itemView.setOnLongClickListener {
            clickListener.onArticleLongClick(article, itemView, this.layoutPosition)
            true
        }
    }
}