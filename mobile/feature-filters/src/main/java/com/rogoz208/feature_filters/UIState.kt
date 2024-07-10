package com.rogoz208.feature_filters

import com.rogoz208.mobile_common.model.Filter

data class UIState(val filter: Filter)

sealed class UIEvent {
    data class LoadData(val resultKey: String, val filter: Filter) : UIEvent()
    data class SaveData(val filter: Filter) : UIEvent()
    data object BackPressed : UIEvent()
}
