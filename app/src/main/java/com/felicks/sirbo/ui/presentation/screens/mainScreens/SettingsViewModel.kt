package com.felicks.sirbo.ui.presentation.screens.mainScreens

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.felicks.sirbo.data.remote.OtpService
import com.felicks.sirbo.domain.repository.PlanRespository
import com.felicks.sirbo.utils.NetworkUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val planRepository: PlanRespository,
    private val otpService: OtpService
) : ViewModel() {

    private val _isOnline = MutableStateFlow(false)  // Inicializa en false
    val isOnline: StateFlow<Boolean> = _isOnline

    private val _isServerOnline = MutableStateFlow(false)
    val isServerOnline: StateFlow<Boolean> = _isServerOnline

    private val _lastSync = MutableStateFlow<String?>(null)
    val lastSync: StateFlow<String?> = _lastSync

    private val _darkMode = MutableStateFlow(false)
    val darkMode: StateFlow<Boolean> = _darkMode

    private val _isCheckingInternet = MutableStateFlow(false)
    val isCheckingInternet: StateFlow<Boolean> = _isCheckingInternet

    private val _isCheckingServer = MutableStateFlow(false)
    val isCheckingServer: StateFlow<Boolean> = _isCheckingServer

    fun toggleDarkMode() {
        _darkMode.value = !_darkMode.value
        // Guarda preferencia con DataStore aquí si quieres
    }

    fun checkConnection() {
        viewModelScope.launch {
            // Lanzar ambas verificaciones en paralelo
            val internetDeferred = async {
                _isCheckingInternet.value = true
                val startTime = System.currentTimeMillis()

                val internetAvailable = withContext(Dispatchers.IO) {
                    NetworkUtils.isInternetAvailable(context)
                }

                val elapsed = System.currentTimeMillis() - startTime
                val minLoadingTime = 500L
                if (elapsed < minLoadingTime) {
                    delay(minLoadingTime - elapsed)
                }
                _isOnline.value = internetAvailable
                _isCheckingInternet.value = false

                internetAvailable
            }

            val serverDeferred = async {
                _isCheckingServer.value = true
                val startTime = System.currentTimeMillis()

                val serverAvailable = withContext(Dispatchers.IO) {
                    NetworkUtils.isOtpServerAvailable(otpService)
                }

                val elapsed = System.currentTimeMillis() - startTime
                // Aquí sin tiempo mínimo o pon uno si quieres
                 val minLoadingTimeServer = 500L
                 if (elapsed < minLoadingTimeServer) delay(minLoadingTimeServer - elapsed)

                _isServerOnline.value = serverAvailable
                _isCheckingServer.value = false

                serverAvailable
            }

            // Esperamos resultados si necesitas para algo
            val internetAvailable = internetDeferred.await()
            val serverAvailable = serverDeferred.await()

            // Si quieres hacer algo en base a resultados aquí...
        }
    }




    fun sincronizarManual() {
        viewModelScope.launch {
            if (!NetworkUtils.isInternetAvailable(context)) {
                // Muestra mensaje de error con Snackbar o similar
                return@launch
            }

            // Simulación: Sincronizar datos
            // planRepository.fetchRoutes()

            val formatter = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
            _lastSync.value = formatter.format(Date())
        }
    }
}
