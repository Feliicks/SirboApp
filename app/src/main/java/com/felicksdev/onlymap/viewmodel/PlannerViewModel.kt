package com.felicksdev.onlymap.viewmodel


import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.felicksdev.onlymap.LocationDetail
import com.felicksdev.onlymap.data.api.OtpService
import com.felicksdev.onlymap.data.models.otpModels.routing.Plan
import com.felicksdev.onlymap.utils.MapConfig
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PlannerViewModel @Inject constructor(
    private val otpApiService: OtpService
) : ViewModel() {


    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    // Definición del estado para el error
    private val _errorState = MutableStateFlow<String?>(null)
    val errorState: StateFlow<String?> = _errorState

    private val _toLocation = MutableStateFlow<LocationDetail>(LocationDetail())
    val toLocation: StateFlow<LocationDetail> = _toLocation

    private val _fromLocation = MutableStateFlow<LocationDetail>(LocationDetail())
    val fromLocation: StateFlow<LocationDetail> = _fromLocation

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

    fun setFromPlace(fromPlace: LocationDetail) {
//        _plannerState.value = _plannerState.value.copy(fromPlace = fromPlace)
        Log.d("PlannerViewModel", "Setting from place: $fromPlace")
        _fromLocation.value = fromPlace
        // Aquí puedes guardar el estado si es necesario
    }

    fun setToPlace(toPlace: LocationDetail) {
//        _plannerState.value = _plannerState.value.copy(toPlace = toPlace)
        Log.d("PlannerViewModel", "Setting to place: $toPlace")
        _toLocation.value = toPlace
        // Aquí puedes guardar el estado si es necesario
    }

    fun swapLocations() {
//        _plannerState.value = _plannerState.value.copy(
//            fromPlace = _plannerState.value.toPlace,
//            toPlace = _plannerState.value.fromPlace
//        )


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
        // Aquí puedes guardar el estado si es necesario
    }

    fun fetchPlan() {
        // Implementa la lógica para fetchPlan aquí
        Log.d("PlannerViewModel", "Fetching plan...")
        val fromLocation = _fromLocation.value
        val toLocation = _toLocation.value
        Log.d(
            "PlannerViewModel",
            "Haciendo plan con  from: ${fromLocation.latitude}, ${fromLocation.longitude}"
        )
        Log.d(
            "PlannerViewModel",
            "Haciendo plan con  to: ${toLocation.latitude}, ${toLocation.longitude}"
        )
        viewModelScope.launch {
            try {
                _isLoading.value = true
                val resultado = otpApiService.fetchItineraries(
                    fromPlace = "${fromLocation.latitude},${fromLocation.longitude}",
                    toPlace = "${toLocation.latitude},${toLocation.longitude}"
//                    fromPlace = "-16.49561, -68.15080",
//                    toPlace = "-16.49397, -68.13571"
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


    fun isPlacesDefined(): Boolean {
        return _fromLocation.value.latitude != 0.0 && _fromLocation.value.longitude != 0.0 &&
                _toLocation.value.latitude != 0.0 && _toLocation.value.longitude != 0.0
    }

    fun testSetLocations() {
        Log.d("PlannerViewModel", "Setting test locations")
//        fromPlace = "-16.49561, -68.15080",
//                    toPlace = "-16.49397, -68.13571"
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
    }


}