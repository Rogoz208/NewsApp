package com.rogoz208.feature_headlines.presentation.articles_list.recycler;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.rogoz208.feature_headlines.R;

public class LoadingViewHolder extends RecyclerView.ViewHolder {

    private final ProgressBar progressBar = itemView.findViewById(R.id.load_more_progress_bar);

    public LoadingViewHolder(@NonNull ViewGroup parent) {
        super(LayoutInflater.from(parent.getContext()).inflate(R.layout.loading_view_holder, parent, false));
    }

    public void setProgressBarVisibility(int visibility) {
        progressBar.setVisibility(visibility);
    }
}
