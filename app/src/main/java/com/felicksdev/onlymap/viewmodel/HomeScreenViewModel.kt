package com.felicksdev.onlymap.viewmodel

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.felicksdev.onlymap.data.api.OtpService
import com.felicksdev.onlymap.data.models.Ruta
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.PolylineOptions
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class HomeScreenViewModel : ViewModel() {


    val rutaData: MutableState<Ruta?> = mutableStateOf(null)
    private fun getRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("http://10.0.2.2:3000/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    fun loadRouteById(rutaId: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val call = getRetrofit().create(OtpService::class.java).getRuta("ruta/$rutaId")
                val ruta: Ruta? = call.body()
                rutaData.value = ruta
                Log.d("HomeScreenViewModel", "Ruta: $ruta")
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    // Manejar el error, si es necesario
                    Log.d("HomeScreenViewModel", "Error: $e")
                }
            }
        }
    }

    suspend fun printRoute(ruta: Ruta?) {
        ruta?.let {
            val polylineOptions = PolylineOptions()

            it.geometria_ruta.coordinates.forEach { coordinates ->
                coordinates.forEach { coordinate ->
                    val latLng = LatLng(coordinate[1], coordinate[0])
                    polylineOptions.add(latLng)
                }
            }

            withContext(Dispatchers.Main) {
                // Limpiar cualquier polilínea existente en el mapa antes de agregar la nueva
                // mMap.clear()

                // Asignar propiedades al PolylineOptions fuera del bucle para evitar duplicaciones
                // polylineOptions.color(color)
                polylineOptions.width(5f) // Ancho de la polilínea (ajusta según tus necesidades)

                // Agregar la polilínea al mapa una sola vez fuera del bucle
                // mMap.addPolyline(polylineOptions)

                // Centrar el mapa en la geometría de la ruta
                // mMap.moveCamera(
                //     CameraUpdateFactory.newLatLngBounds(
                //         getLatLngBounds(ruta.geometria_ruta.coordinates),
                //         10
                //     )
                // )
            }
        }
    }

    // Función para obtener LatLngBounds
    private fun getLatLngBounds(coordinates: List<List<List<Double>>>): LatLngBounds {
        val builder = LatLngBounds.Builder()
        coordinates.forEach { coordinates ->
            coordinates.forEach { coordinate ->
                val latLng = LatLng(coordinate[1], coordinate[0])
                builder.include(latLng)
            }
        }
        return builder.build()
    }

}
