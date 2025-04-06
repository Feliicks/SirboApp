package com.felicksdev.onlymap.viewmodel


import android.content.Context
import android.util.Log
import androidx.datastore.preferences.core.edit
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.felicksdev.onlymap.LocationDetail
import com.felicksdev.onlymap.data.models.otpModels.routing.Itinerary
import com.felicksdev.onlymap.data.models.otpModels.routing.Plan
import com.felicksdev.onlymap.data.preferences.OtpPreferenceKeys
import com.felicksdev.onlymap.data.preferences.dataStore
import com.felicksdev.onlymap.domain.models.OtpConfig
import com.felicksdev.onlymap.domain.repository.PlanRespository
import com.felicksdev.onlymap.isSetted
import com.felicksdev.onlymap.utils.MapConfig
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.net.UnknownHostException
import javax.inject.Inject

@HiltViewModel
class PlannerViewModel @Inject constructor(
    private val planRepository: PlanRespository
) : ViewModel() {
    private val _config = MutableStateFlow(OtpConfig())
    val config: StateFlow<OtpConfig> = _config

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    // Definición del estado para el error
    private val _errorState = MutableStateFlow<String?>(null)
    val errorState: StateFlow<String?> = _errorState

    private val _toLocation = MutableStateFlow<LocationDetail>(LocationDetail())
    val toLocation: StateFlow<LocationDetail> = _toLocation

    private val _fromLocation = MutableStateFlow<LocationDetail>(LocationDetail())
    val fromLocation: StateFlow<LocationDetail> = _fromLocation

    private val _isLocationDefined = MutableStateFlow<Boolean>(false)
    val isLocationDefined: StateFlow<Boolean> = _isLocationDefined

    private val _itineraries = MutableStateFlow<List<Itinerary>>(emptyList())
    val itineraries: StateFlow<List<Itinerary>> = _itineraries

    private val _itinerarySelectedIndex = MutableStateFlow<Int>(-1)
    val itinerarySelectedIndex: StateFlow<Int> = _itinerarySelectedIndex

    private val _selectedItinerary = MutableStateFlow<Itinerary?>(Itinerary())
    val selectedItinerary: StateFlow<Itinerary?> = _selectedItinerary

    fun setItinerarySelected(itinerary: Itinerary) {
        _selectedItinerary.value = itinerary
    }

    // Después de hacer fetch
    private val _planResult = MutableStateFlow<Plan?>(null)
    val planResult: StateFlow<Plan?> = _planResult.asStateFlow()

    private val _cameraPosition = MutableStateFlow(
        CameraPosition.fromLatLngZoom(
            LatLng(
                MapConfig.initialState.position.target.latitude,
                MapConfig.initialState.position.target.longitude
            ),
            MapConfig.initialState.position.zoom
        )
    )
    val cameraPosition: StateFlow<CameraPosition> = _cameraPosition.asStateFlow()

    // Función para actualizar la posición de la cámara
    fun updateCameraPosition(newPosition: LatLng) {
//        _cameraPosition.value = n ewPosition
    }

    fun setFromPlace(fromPlace: LocationDetail) {
        Log.d("PlannerViewModel", "Setting from place: $fromPlace")
        _fromLocation.value = fromPlace
        updatePlacesDefinedState()
    }

    fun loadConfig(context: Context) {
        viewModelScope.launch {
            context.dataStore.data.map { prefs ->
                val config = OtpConfig(
                    mode = prefs[OtpPreferenceKeys.MODE] ?: "TRANSIT,WALK",
                    walkDistance = prefs[OtpPreferenceKeys.WALK_DISTANCE] ?: 1000,
                    maxTransfers = prefs[OtpPreferenceKeys.MAX_TRANSFERS] ?: 1,
                    numItineraries = prefs[OtpPreferenceKeys.NUM_ITINERARIES] ?: 3
                )

                // 🔍 LOG al cargar configuración
                Log.d("OtpConfig", "Config cargada desde DataStore: $config")

                config
            }.collect { loadedConfig ->
                _config.value = loadedConfig
            }
        }
    }

    fun saveConfig(context: Context, config: OtpConfig) {
        viewModelScope.launch {
            context.dataStore.edit { prefs ->
                prefs[OtpPreferenceKeys.MODE] = config.mode
                prefs[OtpPreferenceKeys.WALK_DISTANCE] = config.walkDistance
                prefs[OtpPreferenceKeys.MAX_TRANSFERS] = config.maxTransfers
                prefs[OtpPreferenceKeys.NUM_ITINERARIES] = config.numItineraries
            }

            // 🔍 Log al guardar configuración
            Log.d("OtpConfig", "Configuración guardada: $config")

            _config.value = config
        }
    }

    fun resetConfig(context: Context) {
        viewModelScope.launch {
            context.dataStore.edit { prefs ->
                prefs.remove(OtpPreferenceKeys.MODE)
                prefs.remove(OtpPreferenceKeys.WALK_DISTANCE)
                prefs.remove(OtpPreferenceKeys.MAX_TRANSFERS)
                prefs.remove(OtpPreferenceKeys.NUM_ITINERARIES)
            }

            val defaultConfig = OtpConfig()
            _config.value = defaultConfig

            Log.d("OtpConfig", "Configuración restablecida a valores por defecto: $defaultConfig")
        }
    }


    private fun updatePlacesDefinedState() {
        val isDefined = _fromLocation.value.isSetted() && _toLocation.value.isSetted()
        Log.d("PlannerViewModel", "is placed defined $isDefined")
        _isLocationDefined.value = isDefined
    }

    fun setToPlace(toPlace: LocationDetail) {
//        _plannerState.value = _plannerState.value.copy(toPlace = toPlace)
        Log.d("PlannerViewModel", "Setting to place: $toPlace")
        _toLocation.value = toPlace
        // Aquí puedes guardar el estado si es necesario
        updatePlacesDefinedState()
    }

    fun swapLocations() {
        val fromLocation = _fromLocation.value
        val toLocation = _toLocation.value

        if (fromLocation != null && toLocation != null) {
            _fromLocation.value = toLocation
            _toLocation.value = fromLocation
        } else {
            Log.e(
                "swapLocations",
                "No se pueden intercambiar ubicaciones: uno de los valores es nulo"
            )
        }
    }

    fun reset() {
        _planResult.value = null
        _fromLocation.value = LocationDetail()
        _toLocation.value = LocationDetail()
        _selectedItinerary.value = Itinerary()
        updatePlacesDefinedState()
    }

    fun fetchPlan() {
        Log.d("PlannerViewModel", "Iniciando fetchPlan...")

        val fromLocation = _fromLocation.value
        val toLocation = _toLocation.value

        // Validación: Revisar si las ubicaciones están definidas
        if (fromLocation == null || toLocation == null) {
            _errorState.value = "Ubicación de origen o destino no establecida."
            Log.e("PlannerViewModel", "Error: La ubicación de origen o destino es nula.")
            return
        }

        Log.d(
            "PlannerViewModel",
            "Planificando ruta desde: ${fromLocation.latitude}, ${fromLocation.longitude}"
        )
        Log.d(
            "PlannerViewModel",
            "Planificando ruta hasta: ${toLocation.latitude}, ${toLocation.longitude}"
        )

        viewModelScope.launch {
            try {
                _isLoading.value = true
                val resultado = planRepository.fetchPlan(
                    from = "${fromLocation.latitude},${fromLocation.longitude}",
                    to = "${toLocation.latitude},${toLocation.longitude}",
                    config = _config.value
                )
                Log.d(
                    "PlannerViewModel",
                    "Resultado de la solicitud: ${Gson().toJson(resultado.body())}"
                )

                if (!resultado.isSuccessful) {
                    _errorState.value =
                        "Error del servidor: ${resultado.code()} - ${resultado.message()}"
                    Log.e(
                        "PlannerViewModel",
                        "Error HTTP ${resultado.code()}: ${resultado.message()}"
                    )
                    _isLoading.value = false
                    return@launch
                }

                // 🛑 Verificar si el body es nulo (caso de servidor apagado)
                val planResponse = resultado.body()

                if (planResponse == null) {
                    _errorState.value = "El servidor no respondió correctamente (respuesta vacía)."
                    Log.e("PlannerViewModel", "Error: la API devolvió un cuerpo vacío.")
                    _isLoading.value = false

                    return@launch
                }
// 🆕 Verificar si hubo error PATH_NOT_FOUND o noPath = true
                if (planResponse.error?.noPath == true) {
                    _errorState.value = "No se encontró una ruta disponible con los parámetros actuales. Intenta aumentar la distancia máxima para caminar o elige otra ubicación."
                    Log.e("PlannerViewModel", "Error: no se encontró una ruta válida - ${planResponse.error?.msg}")
                    _isLoading.value = false
                    _planResult.value = null
                    _itineraries.value = emptyList()
                    _selectedItinerary.value = null
                    return@launch
                }
                // 🛑 Verificar si el `plan` es nulo o no tiene itinerarios
                val plan: Plan = planResponse.plan
                _planResult.value = plan
                if (plan.itineraries.isNullOrEmpty()) {
                    _errorState.value = "No se encontraron rutas disponibles."
                    Log.e("PlannerViewModel", "Error: No se recibieron itinerarios.")
                    _isLoading.value = false
                    return@launch
                }
                _itineraries.value = plan.itineraries
                _selectedItinerary.value = plan.itineraries.first()
                _isLoading.value = false
                Log.d(
                    "PlannerViewModel",
                    "Plan obtenido con ${plan.itineraries.size} itinerarios."
                )

            } catch (e: UnknownHostException) {
                // 🛑 Manejar error cuando el servidor está apagado o sin internet
                _errorState.value = "No se pudo conectar con el servidor. Verifica tu conexión."
                Log.e("PlannerViewModel", "Error de conexión con el servidor", e)
                _isLoading.value = false

            } catch (e: Exception) {
//                _errorState.value = "Error al obtener rutas: ${e.localizedMessage}"
                Log.e("PlannerViewModel", "Excepción al obtener rutas", e)
                _isLoading.value = false
            }
        }
    }


    fun fetchPlanWithConfig(config: OtpConfig) {
        val fromLocation = _fromLocation.value
        val toLocation = _toLocation.value

        if (fromLocation == null || toLocation == null) {
            _errorState.value = "Ubicación de origen o destino no establecida."
            return
        }

        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = planRepository.fetchPlan(
                    from = "${fromLocation.latitude},${fromLocation.longitude}",
                    to = "${toLocation.latitude},${toLocation.longitude}",
                    config = config
                )

                val planResponse = response.body()
                if (!response.isSuccessful || planResponse == null) {
                    _errorState.value = "Error del servidor o respuesta vacía."
                    _planResult.value = null                   // 🔥 limpia la ruta
                    _itineraries.value = emptyList()           // 🔥 limpia la lista
                    return@launch
                }

                if (planResponse.error?.noPath == true) {
                    _errorState.value = "No se encontró una ruta disponible con los parámetros actuales. Intenta aumentar la distancia máxima o elige otra ubicación."
                    _planResult.value = null                   // 🔥 limpia la ruta
                    _itineraries.value = emptyList()           // 🔥 limpia la list
                    return@launch
                }

                val plan = planResponse.plan
                if (plan.itineraries.isNullOrEmpty()) {
                    _errorState.value = "No se encontraron rutas disponibles."
                    _planResult.value = null                   // 🔥 limpia la ruta
                    _itineraries.value = emptyList()           // 🔥 limpia la list
                    return@launch
                }

                _itineraries.value = plan.itineraries
                _selectedItinerary.value = plan.itineraries.first()
                _planResult.value = plan
                _errorState.value = null
            } catch (e: Exception) {
                _errorState.value = "Error: ${e.localizedMessage}"
            } finally {
                _isLoading.value = false
            }
        }
    }



    // Función para limpiar el error después de mostrarlo
    fun clearError() {
        _errorState.value = null
    }
@Deprecated("no es reactivo")
    fun isPlacesDefined(): Boolean {
        return _fromLocation.value.latitude != 0.0 && _fromLocation.value.longitude != 0.0 &&
                _toLocation.value.latitude != 0.0 && _toLocation.value.longitude != 0.0
    }

    fun testSetLocations() {
        Log.d("PlannerViewModel", "Setting test locations")
        _fromLocation.value = LocationDetail(
            description = "Origen",
            latitude = -16.49561,
            longitude = -68.15080,
        )
        _toLocation.value = LocationDetail(
            description = "Destination",
            latitude = -16.49397,
            longitude = -68.13571
        )
        updatePlacesDefinedState()
    }
}