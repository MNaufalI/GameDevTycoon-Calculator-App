@file:Suppress("PropertyName")

package com.naufal.gdtcalculator.ui.base

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

abstract class BaseViewModel : ViewModel(), InterfaceSearchable {
    protected val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    override fun onSearchQueryChange(query: String) {
        _searchQuery.value = query
    }

    override fun clearSearch() {
        _searchQuery.value = ""
    }
    abstract fun clearSelection()
}