package com.felicks.sirbo.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class MainViewModel : ViewModel() {

    private val _showBottomBar = MutableStateFlow(true)
    val showBottomBar: StateFlow<Boolean> = _showBottomBar
    private val _showBottomSheet = mutableStateOf(false)
    var showBottomSheet  = _showBottomSheet
    fun setShowBottomBar(show: Boolean) {
        _showBottomBar.value = show
    }
}