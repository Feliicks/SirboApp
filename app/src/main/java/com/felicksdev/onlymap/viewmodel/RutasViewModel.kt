package com.felicksdev.onlymap.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.felicksdev.onlymap.data.network.MyApiService
import com.felicksdev.onlymap.data.models.Ruta
import com.felicksdev.onlymap.data.models.RutaState
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.math.log

class RutasViewModel (
    private val MyApiService: MyApiService
) : ViewModel() {
    var state by mutableStateOf(RutaState())
        private set
    private val _routes = MutableLiveData<List<Ruta>>()
    val routes: LiveData<List<Ruta>> get() = _routes
    init {
        viewModelScope.launch {
            try {
                val rutas = MyApiService.getAllRutas().body() ?: emptyList()
                state=state.copy(
                    rutas = rutas,
                )
            }
            catch (e: Exception) {
                // Manejar el error según sea necesario
                Log.e("RutasViewModel", "Error al obtener las rutas", e)
            }
        }
    }
    fun onRouteItemSelected(rutas: Ruta) {
        viewModelScope.launch {
            try {
                state = state.copy()
            } catch (e: Exception) {
                Log.e("RutasViewModel", "Error al seleccionar la ruta", e)
            }
        }
    }

//    fun getRoutes() {
//        viewModelScope.launch {
//            try {
//                val call = getRetrofit().create(MyApiService::class.java).getAllRutas()
//                val rutas: List<Ruta>? = call.body()
//                if (call.isSuccessful && !rutas.isNullOrEmpty()) {
//                    _routes.value = rutas
//                } else {
//                    // Manejar el error según sea necesario
//
//                }
//            } catch (e: Exception) {
//                // Manejar el error según sea necesario
//            }
//        }
//    }

    private fun getRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("http://10.0.2.2:3000/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}
