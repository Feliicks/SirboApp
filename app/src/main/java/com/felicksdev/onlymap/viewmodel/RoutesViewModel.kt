package com.felicksdev.onlymap.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.felicksdev.onlymap.data.api.OtpService
import com.felicksdev.onlymap.data.models.AddressState
import com.felicksdev.onlymap.data.models.Ruta
import com.felicksdev.onlymap.data.models.RutaState
import com.felicksdev.onlymap.data.models.otpModels.PatterDetail
import com.felicksdev.onlymap.data.models.otpModels.Pattern
import com.felicksdev.onlymap.data.models.otpModels.RouteStopItem
import com.felicksdev.onlymap.data.models.otpModels.routes.PatternGeometry
import com.felicksdev.onlymap.data.models.otpModels.routes.RoutesItem
import com.felicksdev.onlymap.data.models.otpModels.routing.Leg
import com.felicksdev.onlymap.utils.MapConfig
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.net.SocketTimeoutException
import javax.inject.Inject

@HiltViewModel
class RoutesViewModel @Inject constructor(
    private val otpService: OtpService
) : ViewModel() {

    var state by mutableStateOf(RutaState())
        private set

    private val _allRoutesList = MutableStateFlow<List<RoutesItem>>(emptyList())
    val allRoutesList: StateFlow<List<RoutesItem>> = _allRoutesList

    private val _filteredRoutesList = MutableStateFlow<List<RoutesItem>>(emptyList())
    val filteredRoutesList: StateFlow<List<RoutesItem>> = _filteredRoutesList

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _selectedPatternGeometry = MutableStateFlow(PatternGeometry())
    val selectedPatternGeometry: StateFlow<PatternGeometry> = _selectedPatternGeometry.asStateFlow()


    var routeStops by mutableStateOf<List<RouteStopItem>>(emptyList())
        private set

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


//    private val _errorState = MutableStateFlow<String?>(null)
//    val errorState : String = _errorState.asStateFlow()
//
//    private val _errorState = MutableStateFlow<String?>(null)
//        val errorState : String?= _errorState.value


    private val _errorState = MutableStateFlow<String?>(null)
    val errorState: StateFlow<String?> = _errorState.asStateFlow()


    // Function to update data
    fun updateErrorState(newData: String?) {
        _errorState.value = newData
    }

    private val _routeSelected = MutableStateFlow<RoutesItem>(RoutesItem())
    val routeSelected: MutableStateFlow<RoutesItem> = _routeSelected

    private val _errorMessage = MutableStateFlow<String?>(null) // Estado para manejar errores
    val errorMessage: MutableStateFlow<String?> = _errorMessage

    var routePatterns by mutableStateOf(listOf<Pattern>())
        private set

    var _routeSelectePattern = MutableStateFlow<PatterDetail>(PatterDetail())
    var routeSelectePattern: MutableStateFlow<PatterDetail> = _routeSelectePattern

    var optimalRouteLegs by mutableStateOf<List<Leg>>(emptyList())
        private set

    private val _isLoading = MutableStateFlow<Boolean>(false) // Estado para manejar errores
    val isLoading: MutableStateFlow<Boolean> = _isLoading


    fun setSearchQuery(query: String) {
        _searchQuery.value = query
        filtrarRutas(query) // 🔥 Llamamos a la función de filtrado local
    }

    private fun filtrarRutas(query: String) {
        Log.d("RutasViewModel", "Filtrando rutas con query: $query")
        val filtered = if (query.isEmpty()) {
            _allRoutesList.value // ✅ Ahora accedemos a la lista interna
        } else {
            _allRoutesList.value.filter {
                it.shortName.contains(query, ignoreCase = true) || it.longName.contains(
                    query,
                    ignoreCase = true
                )
            }
        }
        _filteredRoutesList.value = filtered // ✅ Creamos un nuevo `Routes`
    }

    fun onRouteItemSelected(ruta: Ruta) {
        viewModelScope.launch {
            try {
                // Lógica para manejar la selección de una ruta, si es necesario
                // Puedes actualizar el estado u realizar otras acciones aquí
                state = state.copy()
            } catch (e: Exception) {
                Log.e("RutasViewModel", "Error al seleccionar la ruta", e)
            }
        }
    }


    fun setRouteSelected(ruta: RoutesItem) {
        _routeSelected.value = ruta
    }

    fun obtenerRutas() {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val resultado = otpService.indexRoutes()

                _allRoutesList.value = resultado.body() ?: emptyList()
                _filteredRoutesList.value = resultado.body() ?: emptyList()

                Log.d("RutasViewModel", "Rutas obtenidas $allRoutesList")
                Log.d("RutasViewModel", "Obtuve todas las rutas exitosamente")

            } catch (e: SocketTimeoutException) {
                Log.e("RutasViewModel", "Error de conexión: ${e.message}")
                _errorMessage.value = "No se pudo conectar con el servidor. Verifica la red."

            } catch (e: Exception) {
                Log.e("RutasViewModel", "Error al obtener las rutas", e)
                _errorMessage.value = "Ocurrió un error: ${e.message}"
            } finally {
                _isLoading.value = false // 🔥 Asegura que la barra de carga se desactiva SIEMPRE
            }
        }
    }


    fun getRouteGeometry(patternId: String) {
        viewModelScope.launch {
            try {
                val resultado = otpService.getGeomByPattern("$patternId::01")
                _selectedPatternGeometry.value = resultado.body() ?: PatternGeometry()
                // Manejar el caso en que la llamada no fue exitosa

            } catch (e: Exception) {
//                Actualizar error state
            }
        }
    }
    //    fun getRouteStops(id : String) {
//        viewModelScope.launch {
//            try {
//                val resultado = RetrofitHelper.getRetrofit().getRouteStops(id)
////                Log.d("RutasViewModel", "el id enviado es  $id")
//                if (resultado.isSuccessful) {
//                    routeStops = resultado.body() ?: emptyList()
//                    Log.d("RutasViewModel", "paradas o obtenidas $routeStops")
//                    Log.d("RutasViewModel", "Obtuve todas las paradas exitosamente")
//
//                } else {
//                    // Manejar el caso en que la llamada no fue exitosa
//                    Log.e("RutasViewModel", "Resulta !issuccessfulError al obtener las rutas: ${resultado.message()}")
//                }
//            } catch (e: Exception) {
//                // Manejar errores, por ejemplo, emitir un estado de error
//                Log.e("RutasViewModel", "Error al obtener las rutas", e)
//            }
//        }
//    }
//    / Inicializa patternSelectedId como una lista mutable de Pattern


    fun getRouteDetails(id: String) {
        viewModelScope.launch {
            try {
                val resultado = otpService.getRouteDetail(id)
                _routeSelected.value = resultado
                Log.d("RutasViewModel", "Detalle de ruta" + resultado)
            } catch (e: Exception) {
                // Manejar errores, por ejemplo, emitir un estado de error
                Log.e("RutasViewModel", "Error al Obtner detalle de ruta", e)
            }
        }
    }

    fun getRouteStops(id: String) {
        Log.d("RutasViewModel", "Pasando id ruta" + id)
        viewModelScope.launch {
            try {
                val resultado = otpService.getPatternByRouteId(id)
                Log.d("RutasViewModel", "primer rqe enviado es " + resultado)
                if (resultado.isSuccessful) {
                    routePatterns = resultado.body() ?: emptyList()
                    Log.d("RutasViewModel", "paradas o obtenidas $routePatterns")
                    var patternRespose = otpService
                        .getPatternDetailsByPatternId(routePatterns[0].id)
                    Log.d("RutasViewModel", "segundo rqe enviado es " + patternRespose)
                    _routeSelectePattern.value = patternRespose.body()!!

                    Log.d("RutasViewModel", "paradas o obtenidas $routeSelectePattern")

                } else {
                    // Manejar el caso en que la llamada no fue exitosa
                    Log.e(
                        "RutasViewModel",
                        "Resulta !issuccessfulError route soptss: ${resultado.message()}"
                    )
                }
            } catch (e: Exception) {
                // Manejar errores, por ejemplo, emitir un estado de error
                Log.e("RutasViewModel", "Error al route stops", e)
            }
        }
    }

    fun LatLng.toApiString(): String {
        return "${this.latitude},${this.longitude}"
    }

    fun getOptimalRoutes(fromLocation: AddressState, toLocation: AddressState) {
        viewModelScope.launch {
            try {
                val resultado = otpService.getOptimalRoutes(
                    fromLocation.coordinates.toApiString(), toLocation.coordinates.toApiString()
                )
                Log.d("RutasViewModel", "resultado de la ruta optima es " + resultado.body())
//            TODO:
//             realiza la validacion en cuaso de existe el objeto error en lugar de otro de los itinerarios
                val rutas = resultado.body()?.plan!!.itineraries[0].legs
                optimalRouteLegs = resultado.body()?.plan!!.itineraries[0].legs
                Log.d("RutasViewModel", "los itinerarios es  $rutas")
            } catch (e: Exception) {
                Log.e("RutasViewModel", "Error al obtener las rutas", e)
                _errorState.value = "Ocurrió un error con el servidor ${e.message}"
            }

        }

    }

    fun clearError() {
        _errorState.value = null
    }

}
