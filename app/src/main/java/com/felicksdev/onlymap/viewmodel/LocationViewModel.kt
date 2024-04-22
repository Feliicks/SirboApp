package com.felicksdev.onlymap.viewmodel

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.app.ActivityCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.felicksdev.onlymap.data.models.LocationInfo
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.launch


class LocationViewModel
    (

) : ViewModel() {
    lateinit var fusedLocationClient: FusedLocationProviderClient
    lateinit var geoCoder: Geocoder
    var locationState by mutableStateOf<LocationState>(LocationState.NoPermission)

    private val _destinoLocation = MutableLiveData<LocationInfo>()
    val destinoLocation : LiveData<LocationInfo> = _destinoLocation
    private val _origenLocation = MutableLiveData<LocationInfo>()
    val origenLocation : LiveData<LocationInfo> = _origenLocation

    var destinoAddressText by mutableStateOf("")
    var destinoCoordinates by mutableStateOf(LatLng(0.0, 0.0))


    private val _originAddressText = MutableLiveData<String>()
    val originAddressText : LiveData<String> = _originAddressText
    var origenCoordinates by mutableStateOf(LatLng(0.0, 0.0))

    private val _origenFieldSelected = MutableLiveData<Boolean>()
    val origenFieldSelected : LiveData<Boolean> = _origenFieldSelected

    private val _destinoFieldSelected   = MutableLiveData<Boolean>()
    val destinoFieldSelected : LiveData<Boolean> = _destinoFieldSelected

    private val _isButtonEnable = MutableLiveData<Boolean>()
    val isButtonEnable : LiveData<Boolean> = _isButtonEnable


    private val _currentLocation   = MutableLiveData<LatLng>()
    val currentLocation : LiveData<LatLng> = _currentLocation


    fun initializeGeoCoder(context: Context) {
        geoCoder = Geocoder(context)
    }
    fun getInitLocation (context: Context){
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        fusedLocationClient.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, null)
            .addOnSuccessListener { location ->
                locationState =
                    if (location == null && locationState !is LocationState.LocationAvailable) {
                        LocationState.Error
                    } else {
                        _currentLocation.value = LatLng(location.latitude, location.longitude)
                        Log.d("LocationViewModel", currentLocation.value.toString())
                        LocationState.LocationAvailable(
                            LatLng(
                                location.latitude,
                                location.longitude
                            )
                        )

                    }

            }
    }
    fun onLocationChanged (location: LatLng){
        Log.d("LocationViewModel", "Location Changed")
        origenCoordinates = location
        getAddressOrigen(origenCoordinates)
        _origenLocation.value = LocationInfo(
            LatLng(location.latitude, location.longitude),
            getAddressOrigen(origenCoordinates).toString()
        )
    }
    fun getLastLocation(context: Context) {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        fusedLocationClient.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, null)
            .addOnSuccessListener { location ->
                locationState =
                    if (location == null && locationState !is LocationState.LocationAvailable) {
                        LocationState.Error
                    } else {
                        LocationState.LocationAvailable(
                            LatLng(
                                location.latitude,
                                location.longitude
                            )
                        )
                    }
                //Log.d("LocationViewModel", location.toString())
                origenCoordinates = LatLng(location.latitude, location.longitude)
                getAddressOrigen(origenCoordinates)
                _origenLocation.value = LocationInfo(
                    LatLng(location.latitude, location.longitude),
                    getAddressOrigen(origenCoordinates).toString()
                )
                //origenAddressText = geoCoder.getFromLocation(origenCoordinates,1)
            }
    }

    fun onLocationFieldsChange(origen : String, destino : String) {
        _originAddressText.value = origen
        _originAddressText.value = destino
        _isButtonEnable.value = isOrigenFieldValid() && isDestinoFieldValid()
    }
    fun getAddressDestino(latLng: LatLng) {
        viewModelScope.launch {
            val address = geoCoder.getFromLocation(latLng.latitude, latLng.longitude, 1)
            //Log.d("LocationViewModel", address.toString())
            if (address!!.isNotEmpty()) {
                val addressLine = address!![0].getAddressLine(0)
                Log.d("LocationViewModel Destino", addressLine.toString())
                destinoAddressText = addressLine
            } else {
                //Log.d("LocationViewModel", "No address found")
                destinoAddressText = "Direccion no disponible"
            }
            //text = address?.get(0)?.getAddressLine(0) ?: ""
        }
    }
    fun getAddressOrigen(latLng: LatLng) {
        viewModelScope.launch {
            val address = geoCoder.getFromLocation(latLng.latitude, latLng.longitude, 1)
            //Log.d("LocationViewModel", address.toString())
            if (address!!.isNotEmpty()) {
                val addressLine = address!![0].getAddressLine(0)
                Log.d("LocationViewModel Origen", addressLine.toString())
                _originAddressText.value = addressLine
                _origenLocation.value = LocationInfo(
                    latLng,
                    addressLine
                )
            } else {
                Log.d("LocationViewModel", "No address found")
                _originAddressText.value = "Direccion no disponible"
            }
            //text = address?.get(0)?.getAddressLine(0) ?: ""
        }
    }

    fun onDestinoSelected(){
        Log.d("LocationViewModel", "Destino Field seleccionado")

        _destinoFieldSelected.value = true
        _origenFieldSelected.value = false
    }

    fun onOrigenSelected(){
        Log.d("LocationViewModel", "Origen Field seleccionado")
        _destinoFieldSelected.value = false
        _origenFieldSelected.value = true
    }

    fun isOrigenFieldValid() : Boolean{
        return true
    }
    fun isDestinoFieldValid() : Boolean{
        return true
    }
}
