package com.rogoz208.feature_sources.sources_screen.recycler

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bumptech.glide.Glide
import com.rogoz208.feature_sources.R
import com.rogoz208.feature_sources.databinding.SourceViewHolderBinding
import com.rogoz208.mobile_common.model.SourceUI

class SourceViewHolder(
    parent: ViewGroup,
    private val clickListener: OnSourceItemClickListener
) : RecyclerView.ViewHolder(
    LayoutInflater.from(parent.context).inflate(R.layout.source_view_holder, parent, false)
) {

    private val binding by viewBinding(SourceViewHolderBinding::bind)

    fun bind(source: SourceUI) {
        val sourceDescription = "${source.category} | ${source.country}"

        Glide.with(itemView).load(source.urlToImage)
            .placeholder(com.rogoz208.mobile_common.R.drawable.image_placeholder)
            .circleCrop()
            .into(binding.sourceImageView)

        binding.sourceTitleTextView.text = source.name
        binding.sourceDescriptionTextView.text = sourceDescription

        itemView.setOnClickListener {
            clickListener.onSourceClickListener(source, this.layoutPosition)
        }
        itemView.setOnLongClickListener {
            clickListener.onSourceLongClickListener(source, itemView, this.layoutPosition)
            true
        }
    }
}