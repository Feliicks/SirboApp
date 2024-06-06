package com.felicksdev.onlymap.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class MainViewModel : ViewModel() {

    private val _showBottomBar = MutableStateFlow(true)
    val showBottomBar: StateFlow<Boolean> = _showBottomBar

    fun setShowBottomBar(show: Boolean) {
        _showBottomBar.value = show
    }
}