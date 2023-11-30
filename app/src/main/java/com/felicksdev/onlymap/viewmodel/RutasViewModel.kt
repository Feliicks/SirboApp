import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.felicksdev.onlymap.core.RetrofitHelper
import com.felicksdev.onlymap.data.models.Ruta
import com.felicksdev.onlymap.data.models.RutaState
import kotlinx.coroutines.launch

class RutasViewModel : ViewModel() {

    var state by mutableStateOf(RutaState())
        private set

    var routesList by mutableStateOf<List<Ruta>>(emptyList())
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
                    Log.e("RutasViewModel", "Resulta !issuccessfulError al obtener las rutas: ${resultado.message()}")
                }
            } catch (e: Exception) {
                // Manejar errores, por ejemplo, emitir un estado de error
                Log.e("RutasViewModel", "Error al obtener las rutas", e)
            }
        }
    }
}
