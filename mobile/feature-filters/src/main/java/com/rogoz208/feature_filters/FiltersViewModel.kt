package com.rogoz208.feature_filters

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.rogoz208.feature_filters.UIEvent.*
import com.rogoz208.navigation_api.IRouter

class FiltersViewModel(private val router: IRouter) : ViewModel() {

    private val _newUIState = MutableLiveData<UIState>()
    val newUIState: LiveData<UIState>
        get() = _newUIState

    private var resultKey: String? = null

    fun obtainEvent(event: UIEvent) {
        when (event) {
            is BackPressed -> {
                router.back()
            }

            is LoadData -> {
                _newUIState.value = UIState(event.filter)
                resultKey = event.resultKey
            }
            is SaveData -> {
                resultKey?.let {
                    router.setResult(it, event.filter)
                }
                router.back()
            }
        }
    }
}