package com.felicks.sirbo.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.felicks.sirbo.data.remote.OtpService
import com.felicks.sirbo.data.remote.PhotonService
import com.felicks.sirbo.services.RemoteConfigManager
import com.felicks.sirbo.utils.NetworkUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PlanificaScreenViewModel @Inject constructor(
    //configuracion DAO
//    private val rutaGuardadaDao: RutaGuardadaDao,
    @ApplicationContext private val context: Context,
    private val otpService: OtpService,
    private val photonService: PhotonService,
    private val remoteConfigManager: RemoteConfigManager
) : ViewModel() {
    private val TAG = "PlanificaScreenViewModel";
    private val _errorConnectionMessage = MutableStateFlow<String?>(null)
    val errorConnectionMessage: StateFlow<String?> = _errorConnectionMessage
    private var hasCheckedConnection = false

    fun clearErrorMessage() {
        _errorConnectionMessage.value = null
    }

    fun checkConnectionAndServerOnce() {
        if (hasCheckedConnection) return
        hasCheckedConnection = true

        checkConnectionAndServer()
    }

    init {
        viewModelScope.launch {
            remoteConfigManager.fetchAndStoreBaseUrl()
        }
    }

    fun checkConnectionAndServer() {
        viewModelScope.launch {
            _errorConnectionMessage.value = null // limpiar estado anterior
            val hasInternet = NetworkUtils.isInternetAvailable(context)
            if (!hasInternet) {
                Log.d(TAG, "ERROR NO HAY CNEXOIN")
                _errorConnectionMessage.value = "No hay conexión a internet"
                return@launch
            }
            val response = NetworkUtils.isOtpServerAvailable(otpService)
            if (!response) {
                Log.d(TAG, "NO SERVIVICOS")
                _errorConnectionMessage.value = "No hay servicios disponibles."
            }
        }
    }
}