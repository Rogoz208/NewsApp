package com.rogoz208.feature_headlines.presentation.articles_list.recycler;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.rogoz208.feature_headlines.R;
import com.rogoz208.mobile_common.model.ArticleUI;

public class ArticleViewHolder extends RecyclerView.ViewHolder {
    private final ImageView articleImageView = itemView.findViewById(R.id.article_image_view);
    private final TextView articleDescriptionTextView = itemView.findViewById(R.id.article_description_text_view);
    private final ImageView sourceImageView = itemView.findViewById(R.id.source_image_view);
    private final TextView sourceTitleTextView = itemView.findViewById(R.id.source_title_text_view);

    private final OnArticleItemClickListener clickListener;

    public ArticleViewHolder(@NonNull ViewGroup parent, @NonNull OnArticleItemClickListener clickListener) {
        super(LayoutInflater.from(parent.getContext()).inflate(R.layout.article_view_holder, parent, false));
        this.clickListener = clickListener;
    }

    public void bind(ArticleUI article) {
        Glide.with(itemView)
                .load(article.getUrlToImage())
                .placeholder(R.drawable.image_placeholder)
                .into(articleImageView);
        Glide.with(itemView)
                .load(article.getSourceLogoUrl())
                .placeholder(R.drawable.image_placeholder)
                .circleCrop()
                .into(sourceImageView);
        articleDescriptionTextView.setText(article.getDescription());
        sourceTitleTextView.setText(article.getSourceName());

        itemView.setOnClickListener(view -> clickListener.onItemClick(article, this.getLayoutPosition()));
        itemView.setOnLongClickListener(view -> {
            clickListener.onItemLongClick(article, itemView, this.getLayoutPosition());
            return true;
        });
    }
}
