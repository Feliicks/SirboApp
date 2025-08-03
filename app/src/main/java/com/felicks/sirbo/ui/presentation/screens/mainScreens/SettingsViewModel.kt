package com.felicks.sirbo.ui.presentation.screens.mainScreens

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.felicks.sirbo.domain.repository.PlanRespository
import com.felicks.sirbo.utils.NetworkUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val planRepository: PlanRespository,
) : ViewModel() {

    private val _isOnline = MutableStateFlow(NetworkUtils.isInternetAvailable(context))
    val isOnline: StateFlow<Boolean> = _isOnline

    private val _lastSync = MutableStateFlow<String?>(null)
    val lastSync: StateFlow<String?> = _lastSync

    private val _darkMode = MutableStateFlow(false)
    val darkMode: StateFlow<Boolean> = _darkMode

    fun toggleDarkMode() {
        _darkMode.value = !_darkMode.value
        // Podrías guardar la preferencia con DataStore
    }

    fun sincronizarManual() {
        viewModelScope.launch {
            if (!NetworkUtils.isInternetAvailable(context)) {
                // podrías mostrar un error con un Toast o Snackbar
                return@launch
            }

            // Simulación: Sincronizar datos
//            planRepository.fetchRoutes()

            val formatter = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
            _lastSync.value = formatter.format(Date())
        }
    }
}
