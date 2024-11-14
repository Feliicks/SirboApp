package com.felicksdev.onlymap.viewmodel


import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.felicksdev.onlymap.TrufiLocation
import com.felicksdev.onlymap.data.api.OtpService
import com.felicksdev.onlymap.data.models.RoutePlanner
import com.felicksdev.onlymap.data.models.otpModels.routing.Plan
import com.felicksdev.onlymap.utils.MapConfig
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PlannerViewModel @Inject constructor(
    private val otpApiService: OtpService
)
: ViewModel() {
    private val _plannerState = MutableStateFlow(RoutePlanner())
    val plannerState: StateFlow<RoutePlanner> = _plannerState.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    // Definición del estado para el error
    private val _errorState = MutableStateFlow<String?>(null)
    val errorState: StateFlow<String?> = _errorState


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
//        _cameraPosition.value = newPosition
    }

    fun setFromPlace(fromPlace: TrufiLocation) {
//        _plannerState.value = _plannerState.value.copy(fromPlace = fromPlace)
        Log.d("PlannerViewModel", "Setting from place: $fromPlace")
        _plannerState.update { currentState ->
            currentState.copy(fromPlace = fromPlace)
        }
        // Aquí puedes guardar el estado si es necesario
    }

    fun setToPlace(toPlace: TrufiLocation) {
//        _plannerState.value = _plannerState.value.copy(toPlace = toPlace)
        Log.d("PlannerViewModel", "Setting to place: $toPlace")
        _plannerState.update { currentState ->
            currentState.copy(toPlace = toPlace)
        }
        // Aquí puedes guardar el estado si es necesario
    }

    fun swapLocations() {
        _plannerState.value = _plannerState.value.copy(
            fromPlace = _plannerState.value.toPlace,
            toPlace = _plannerState.value.fromPlace
        )

        // Aquí puedes guardar el estado si es necesario
    }

    fun reset() {
        _planResult.value = null
        _plannerState.update { currentState ->
            currentState.copy(
                fromPlace = null,
                toPlace = null
            )
        }
        // Aquí puedes guardar el estado si es necesario
    }

    fun fetchPlan() {
        // Implementa la lógica para fetchPlan aquí
        Log.d("PlannerViewModel", "Fetching plan...")
        viewModelScope.launch {
            try {
                _isLoading.value = true
                val resultado = otpApiService.fetchItineraries(
//                    fromPlace = _plannerState.value.getFromCoordinates(),
//                    toPlace = _plannerState.value.getToCoordinates()
                    fromPlace = "-16.49561, -68.15080",
                    toPlace = "-16.49397, -68.13571"
                )
                _planResult.value = resultado.body()!!.plan
                val itiniarios = resultado.body()!!.plan.itineraries
                _isLoading.value = false
//                Log.d("PlannerViewModel", "resultado de la ruta optima es " + resultado.body())
                Log.d("PlannerViewModel", "los itinerarios es  $itiniarios")

            } catch (e: Exception) {
                _isLoading.value = false
                _errorState.value = "Ocurrió un error con el servidor ${e.message}"
                Log.e("PlannerViewModel", "Error al obtener las rutas", e)
            }

        }
    }
    // Función para limpiar el error después de mostrarlo
    fun clearError() {
        _errorState.value = null
    }
    fun testIsPlacedDefined(): Boolean {
        return if (_plannerState.value.fromPlace != null && _plannerState.value.toPlace != null) {
            true
        } else {
            false
        }
    }

    fun isPlacesDefined() = _plannerState.value.isPlacesDefined
    fun testSetLocations() {
        _plannerState.value = RoutePlanner(
            fromPlace = TrufiLocation(
                description = "Origen",
                latitude = -17.7833,
                longitude = -63.1667
            ),
            toPlace = TrufiLocation(
                description = "Destination",
                latitude = -17.7833,
                longitude = -63.1667
            ),
        )
    }
}