package com.rogoz208.feature_sources.sources_screen.recycler

import android.view.View
import com.rogoz208.mobile_common.model.SourceUI

interface OnSourceItemClickListener {

    fun onSourceClickListener(item: SourceUI, position: Int)

    fun onSourceLongClickListener(item: SourceUI, itemView: View, position: Int)
}