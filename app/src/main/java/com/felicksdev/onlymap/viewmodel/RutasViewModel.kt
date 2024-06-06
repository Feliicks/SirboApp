import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.felicksdev.onlymap.data.models.Ruta
import com.felicksdev.onlymap.data.models.RutaState
import com.felicksdev.onlymap.data.models.otpModels.PatterDetail
import com.felicksdev.onlymap.data.models.otpModels.Pattern
import com.felicksdev.onlymap.data.models.otpModels.RouteStopItem
import com.felicksdev.onlymap.data.models.otpModels.RoutesModelItem
import com.felicksdev.onlymap.services.network.RetrofitHelper
import kotlinx.coroutines.launch

class RutasViewModel : ViewModel() {

    var state by mutableStateOf(RutaState())
        private set

    var routesList by mutableStateOf<List<RoutesModelItem>>(emptyList())
        private set
    var routeStops by mutableStateOf<List<RouteStopItem>>(emptyList())
        private set


    private val _routeSelected = mutableStateOf(
        RoutesModelItem(
            id = "",
            shortName = "",
            longName = "",
            mode = "",
            agencyName = ""
        )
    )
//    var routeSelected: RoutesModelItem = _routeSelected.value
var routeSelected: RoutesModelItem by _routeSelected
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
    var routeSelectePattern by mutableStateOf(PatterDetail(
        id = "",
        desc = "",
        routeId = "",
        stops = emptyList()
    ))
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
                val resultado = RetrofitHelper.getRetrofit().getAllRutas()
                if (resultado.isSuccessful) {
                    routesList = resultado.body() ?: emptyList()
                    Log.d("RutasViewModel", "Rutas obtenidas $routesList")
                    Log.d("RutasViewModel", "Obtuve todas las rutas exitosamente")

                } else {
                    // Manejar el caso en que la llamada no fue exitosa
                    Log.e(
                        "RutasViewModel",
                        "Resulta !issuccessfulError al obtener las rutas: ${resultado.message()}"
                    )
                }
            } catch (e: Exception) {
                // Manejar errores, por ejemplo, emitir un estado de error
                Log.e("RutasViewModel", "Error al obtener las rutas", e)
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
                val resultado = RetrofitHelper.getRetrofit().getPatternByRouteId(id)
                Log.d("RutasViewModel", "primer rqe enviado es "+resultado)
                if (resultado.isSuccessful) {
                    routePatterns = resultado.body() ?: emptyList()
                    Log.d("RutasViewModel", "paradas o obtenidas $routePatterns")
                    var patternRespose  = RetrofitHelper.getRetrofit().getPatternDetailsByPatternId(routePatterns[0].id)
                    Log.d("RutasViewModel", "segundo rqe enviado es "+patternRespose)
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

}
