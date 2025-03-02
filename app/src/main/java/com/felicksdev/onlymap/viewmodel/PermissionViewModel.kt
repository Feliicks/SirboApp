package com.felicksdev.onlymap.viewmodel

import android.annotation.SuppressLint
import android.location.Location
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class PermissionViewModel @Inject constructor() : ViewModel() {


    private val _location = MutableStateFlow<Location?>(null)
    val location: StateFlow<Location?> = _location

    fun onLocationPermissionGranted() {
        getLastKnownLocation()
    }

    @SuppressLint("MissingPermission")
    private fun getLastKnownLocation() {
//        fusedLocationClient.lastLocation
//            .addOnSuccessListener { loc: Location? ->
//                loc?.let {
//                    _location.value = it
//                    Log.d(
//                        "LocationViewModel",
//                        "Ubicación obtenida: ${it.latitude}, ${it.longitude}"
//                    )
//                }
//            }
    }

}