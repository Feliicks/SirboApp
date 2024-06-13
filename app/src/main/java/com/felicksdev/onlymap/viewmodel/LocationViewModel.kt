package com.felicksdev.onlymap.viewmodel

import android.Manifest
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
import com.felicksdev.onlymap.data.models.AddressState
import com.felicksdev.onlymap.data.models.LocationInfo
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class LocationViewModel
    (

) : ViewModel() {

    val currentAddressState = mutableStateOf(AddressState())
    lateinit var fusedLocationClient: FusedLocationProviderClient
    lateinit var geoCoder: Geocoder
    var locationState by mutableStateOf<LocationState>(LocationState.NoPermission)

//    private val _destinoLocation = MutableLiveData<LocationInfo>()
//    val destinoLocation: LiveData<LocationInfo> = _destinoLocation
//
//    private val _originLocation = MutableLiveData<LocationInfo>()
//    val originLocation: LiveData<LocationInfo> = _originLocation

    private val _destinationLocation = MutableLiveData<LocationInfo>()
    val destinationLocation: LiveData<LocationInfo> = _destinationLocation

    private val _originLocation = MutableLiveData<LocationInfo>()
    val originLocation: LiveData<LocationInfo> = _originLocation


    private val _destinyLocation = MutableLiveData<LocationInfo>()
    val destinyLocation: LiveData<LocationInfo> = _destinyLocation

    private val _currentLocationState = MutableLiveData<AddressState>()
    val currentLocationState: LiveData<AddressState> = _currentLocationState


    var destinoAddressText by mutableStateOf("")
    var destinoCoordinates by mutableStateOf(LatLng(0.0, 0.0))


    private val _originAddressText = MutableLiveData<String>()
    val originAddressText: LiveData<String> = _originAddressText
    var lastKnowedCoordinates by mutableStateOf(LatLng(0.0, 0.0))

    private val _origenFieldSelected = MutableLiveData<Boolean>(false)
    val origenFieldSelected: LiveData<Boolean> = _origenFieldSelected

    private val _destinoFieldSelected = MutableLiveData<Boolean>(true)
    val destinoFieldSelected: LiveData<Boolean> = _destinoFieldSelected

    private val _isButtonEnable = MutableLiveData<Boolean>()
    val isButtonEnable: LiveData<Boolean> = _isButtonEnable


    private val _currentLocation = MutableLiveData<LatLng>()
    val currentLocation: LiveData<LatLng> = _currentLocation


    val originLocationState = mutableStateOf(AddressState())
    val destinationLocationState = mutableStateOf(AddressState())

    fun initializeGeoCoder(context: Context) {
        geoCoder = Geocoder(context)
    }

    fun getInitLocation(context: Context) {
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
                        location
                        LocationState.LocationAvailable(
                            LatLng(
                                location.latitude,
                                location.longitude
                            )
                        )
                    }

            }
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
                lastKnowedCoordinates = LatLng(location.latitude, location.longitude)
                getAddressOrigen(lastKnowedCoordinates)
                _originLocation.value = LocationInfo(
                    LatLng(location.latitude, location.longitude),
                    getAddressOrigen(lastKnowedCoordinates).toString()
                )
                Log.d(
                    "LocationViewModel",
                    "El addres obtenido es: " + _originLocation.value.toString()
                )
                //origenAddressText = geoCoder.getFromLocation(origenCoordinates,1)
                //ejecutar funcion para hacer geocoding con el objeto seleccionado
//                getAddressByLatLng(origenCoordinates)
//                settear con ifs

                viewModelScope.launch {
//                    val address = geoCoder.getFromLocation(location.latitude, location.longitude, 1)
//                    if (address!!.isNotEmpty()) {
//                        val addressLine = address!![0].getAddressLine(0)
//                        Log.d("LocationViewModel", addressLine.toString())
//                        _originAddressText.value = addressLine
//                        _originLocation.value = LocationInfo(
//                            LatLng(location.latitude, location.longitude),
//                            addressLine
//                        )
//                        Log.i("LocationViewModel originInfo", _originLocation.value.toString())
//                    } else {
//                        Log.d("LocationViewModel", "No address found")
//                        _originAddressText.value = "Direccion no disponible"
//                    }
//                    Log.d(
//                        "usando viewmodel scope",
//                        getAddressByLatLng(LatLng(location.latitude, location.longitude))
//                    )
//                    if (_destinoFieldSelected.value!!) {
////                        getAddressDestino(LatLng(location.latitude, location.longitude))
//                        destinationLocationState.value.address =
//                            getAddressByLatLng(LatLng(location.latitude, location.longitude))
//                        destinationLocationState.value.coordinates = LatLng(location.latitude, location.longitude)
//                        Log.d("LocationViewModel", "El address obtenido esta seteando en destination: ${destinationLocationState.value}")
//
//                    }
//                    if (_origenFieldSelected.value!!) {
//                      originLocationState.value.address =
//                            getAddressByLatLng(LatLng(location.latitude, location.longitude))
//                        originLocationState.value.coordinates = LatLng(location.latitude, location.longitude)
//                        Log.d("LocationViewModel", "El address obtenido esta seteando en origin: ${originLocationState.value}")
//
//                    }
//                    Log.d("LocationViewModel", "El address obtenido es: ${originLocationState.value}")
                }

            }
    }

    fun onClickSetMyLocation() {
        viewModelScope.launch {
            var currentLocation = AddressState(
                address = getAddressByLatLng(lastKnowedCoordinates),
                coordinates = lastKnowedCoordinates
            )
            if (_destinoFieldSelected.value!!)
                destinationLocationState.value = currentLocation

            if (_origenFieldSelected.value!!)
                originLocationState.value = currentLocation


        }
//
//        if (_destinoFieldSelected.value!!) {
////                        getAddressDestino(LatLng(location.latitude, location.longitude))
//            destinationLocationState.value.address =
//                getAddressByLatLng(LatLng(location.latitude, location.longitude))
//            destinationLocationState.value.coordinates = LatLng(location.latitude, location.longitude)
//            Log.d("LocationViewModel", "El address obtenido esta seteando en destination: ${destinationLocationState.value}")
//
//        }
//        if (_origenFieldSelected.value!!) {
//            originLocationState.value.address =
//                getAddressByLatLng(LatLng(location.latitude, location.longitude))
//            originLocationState.value.coordinates = LatLng(location.latitude, location.longitude)
//            Log.d("LocationViewModel", "El address obtenido esta seteando en origin: ${originLocationState.value}")
//
//        }
    }



    fun onLocationFieldsChange(origen: String, destino: String) {
        _originAddressText.value = origen
        _originAddressText.value = destino
        _isButtonEnable.value = isOrigenFieldValid() && isDestinoFieldValid()
    }


    suspend fun getAddressByLatLng(latLng: LatLng): String {
        var response: String = "";
        try {
            withContext(Dispatchers.IO) {
                val address = geoCoder.getFromLocation(latLng.latitude, latLng.longitude, 1)
                if (address!!.isNotEmpty()) {
                    val addressLine = address!![0].getAddressLine(0)
                    response = address!![0].getAddressLine(0)
                    Log.d("Locat", addressLine.toString())

                } else {
                    response = "Direccion no disponible"
                }
            }
        } catch (e: Exception) {
            Log.d("LocationViewModel", "Error al obtener la dirección ${e.message}")
        }
        return response
    }

    fun getAddressDestino(latLng: LatLng) {
        viewModelScope.launch {
            val address = geoCoder.getFromLocation(latLng.latitude, latLng.longitude, 1)
            if (address!!.isNotEmpty()) {
                val addressLine = address!![0].getAddressLine(0)
                Log.d("LocationViewModel Destino", addressLine.toString())
                destinoAddressText = addressLine
                _destinyLocation.value = LocationInfo(
                    latLng,
                    addressLine
                )
                Log.d("LocationViewModel Destino", _destinyLocation.toString())
            } else {
                destinoAddressText = "Direccion no disponible"
            }
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
                _originLocation.value = LocationInfo(
                    latLng,
                    addressLine
                )
                Log.i("LocationViewModel originInfo", _originLocation.value.toString())
            } else {
                Log.d("LocationViewModel", "No address found")
                _originAddressText.value = "Direccion no disponible"
            }
        }
    }

    fun onDestinoSelected() {
//        fieldSelected = FieldSelected.Destino
        Log.d("LocationViewModel", "Destino Field seleccionado")

        _destinoFieldSelected.value = true
        _origenFieldSelected.value = false
    }

    fun onOriginSelected() {

        Log.d("LocationViewModel", "Origin Field selecionado")
        _destinoFieldSelected.value = false
        _origenFieldSelected.value = true
    }

    fun isOrigenFieldValid(): Boolean {
        return true
    }

    fun isDestinoFieldValid(): Boolean {
        return true
    }

    fun getCurrentLocation(): LatLng {
        return currentLocation.value!!
    }
}
