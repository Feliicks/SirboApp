
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.felicksdev.onlymap.data.models.AddressState
import com.felicksdev.onlymap.data.models.Ruta
import com.felicksdev.onlymap.data.models.RutaState
import com.felicksdev.onlymap.data.models.otpModels.PatterDetail
import com.felicksdev.onlymap.data.models.otpModels.Pattern
import com.felicksdev.onlymap.data.models.otpModels.RouteStopItem
import com.felicksdev.onlymap.data.models.otpModels.routes.PatternGeometry
import com.felicksdev.onlymap.data.models.otpModels.routes.Routes
import com.felicksdev.onlymap.data.models.otpModels.routes.RoutesItem
import com.felicksdev.onlymap.data.models.otpModels.routing.Leg
import com.felicksdev.onlymap.services.network.RetrofitHelper
import com.felicksdev.onlymap.utils.MapConfig
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


class RoutesViewModel @Inject constructor() : ViewModel() {

    var state by mutableStateOf(RutaState())
        private set

    //    var routesList by mutableStateOf<List<RoutesModelItem>>(emptyList())
//        private set
    private val _routesList = MutableStateFlow(Routes())
    val routesList: StateFlow<Routes> = _routesList.asStateFlow()


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

    private val _routeSelected = mutableStateOf(
        RoutesItem(
            id = "", shortName = "", longName = "", mode = "", agencyName = ""
        )
    )

    //    var routeSelected: RoutesModelItem = _routeSelected.value
    var routeSelected: RoutesItem by _routeSelected
//    var routeSelected: RoutesModelItem by mutableStateOf(
//        RoutesModelItem(
//            id = "",
//            shortName = "",
//            longName = "",
//            mode = "",
//            agencyName = ""
//        )
//    )

    var routePatterns by mutableStateOf(listOf<Pattern>())
        private set
    var routeSelectePattern by mutableStateOf(
        PatterDetail(
            id = "", desc = "", routeId = "", stops = emptyList()
        )
    )
        private set
    var optimalRouteLegs by mutableStateOf<List<Leg>>(emptyList())
        private set

    init {
        obtenerRutas()
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

    private fun obtenerRutas() {
        viewModelScope.launch {
            try {
                val resultado = RetrofitHelper.otpRetrofit().indexRoutes()

                _routesList.value = resultado.body() ?: Routes()
                Log.d("RutasViewModel", "Rutas obtenidas $routesList")
                Log.d("RutasViewModel", "Obtuve todas las rutas exitosamente")

                // Manejar el caso en que la llamada no fue exitosa

            } catch (e: Exception) {
//                manerjar error state
                // Manejar errores, por ejemplo, emitir un estado de error
                Log.e("RutasViewModel", "Error al obtener las rutas", e)
            }
        }
    }


    fun getRouteGeometry(patternId: String) {
        viewModelScope.launch {
            try {
                val resultado = RetrofitHelper.otpRetrofit().getGeomByPattern("$patternId::01")
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

    fun getRouteStops(id: String) {
        viewModelScope.launch {
            try {
                val resultado = RetrofitHelper.otpRetrofit().getPatternByRouteId(id)
                Log.d("RutasViewModel", "primer rqe enviado es " + resultado)
                if (resultado.isSuccessful) {
                    routePatterns = resultado.body() ?: emptyList()
                    Log.d("RutasViewModel", "paradas o obtenidas $routePatterns")
                    var patternRespose = RetrofitHelper.otpRetrofit()
                        .getPatternDetailsByPatternId(routePatterns[0].id)
                    Log.d("RutasViewModel", "segundo rqe enviado es " + patternRespose)
                    routeSelectePattern = patternRespose.body()!!

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
                val resultado = RetrofitHelper.otpRetrofit().getOptimalRoutes(
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
