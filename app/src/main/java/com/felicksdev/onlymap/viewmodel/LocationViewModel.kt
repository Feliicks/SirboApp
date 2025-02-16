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
import com.felicksdev.onlymap.data.api.PhotonService
import com.felicksdev.onlymap.data.models.AddressState
import com.felicksdev.onlymap.data.models.LocationInfo
import com.felicksdev.onlymap.data.models.photonModels.toDomain
import com.felicksdev.onlymap.domain.Place
import com.felicksdev.onlymap.domain.LocationProperties
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class LocationViewModel @Inject constructor(
    private val photonService: PhotonService
) : ViewModel() {
    //
//    Localizacion Actualizada

    private val _startLocation = MutableStateFlow(LocationInfo())
    val startLocation: StateFlow<LocationInfo> = _startLocation.asStateFlow()

    private val _endLocation = MutableStateFlow(LocationInfo())
    val endLocation: StateFlow<LocationInfo> = _endLocation.asStateFlow()

    val originLocationState = mutableStateOf(AddressState())
    val destinationLocationState = mutableStateOf(AddressState())

    //
    val currentAddressState = mutableStateOf(AddressState())
    lateinit var fusedLocationClient: FusedLocationProviderClient

    //    lateinit var geoCoder: Geocoder
    var locationState by mutableStateOf<LocationState>(LocationState.NoPermission)

//    private val _destinoLocation = MutableLiveData<LocationInfo>()
//    val destinoLocation: LiveData<LocationInfo> = _destinoLocation
//
//    private val _originLocation = MutableLiveData<LocationInfo>()
//    val originLocation: LiveData<LocationInfo> = _originLocation

    // Definición del estado para el error
    private val _recentLocations = MutableStateFlow<List<Place>>(emptyList())
    val recentLocations: StateFlow<List<Place>> = _recentLocations

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery

    private val _searchResults = MutableStateFlow<List<Place>>(emptyList())
    val searchResults: StateFlow<List<Place>> = _searchResults

    private val _recentPlaces = MutableStateFlow<List<Place>>(emptyList())
    val recentPlaces: StateFlow<List<Place>> = _recentPlaces


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

    private val _currentAddress = MutableStateFlow<LocationProperties>(LocationProperties())
    val currentAddress: MutableStateFlow<LocationProperties> = _currentAddress


    private lateinit var geocoder: Geocoder
    fun initializeGeoCoder(context: Context) {
        geocoder = Geocoder(context)
    }

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
        if (query.isNotEmpty()) {
            searchLocations(query)
        } else {
            _searchResults.value = emptyList()
        }
    }

    private fun searchLocations(query: String) {
        viewModelScope.launch {
//            val results = locationRepository.searchPlaces(query)
//            _searchResults.value = results
        }
    }

    fun selectLocation(place: Place) {
        // Guardar la ubicación seleccionada
    }

    // Función para obtener el LocationInfo basado en la selección
    fun getSelectedLocationInfo(): LocationInfo {
        return if (destinoFieldSelected.value!!) {
            _destinationLocation.value ?: LocationInfo() // Valor predeterminado
        } else {
            _originLocation.value ?: LocationInfo() // Valor predeterminado
        }
    }

    fun updateLocationInfo(locationInfo: LocationInfo) {
        viewModelScope.launch {
            try {
                val addressList = geocoder.getFromLocation(
                    locationInfo.coordinates.latitude,
                    locationInfo.coordinates.longitude,
                    1
                )
                val updatedLocationInfo = locationInfo.copy(
                    address = addressList?.get(0)?.getAddressLine(0) ?: "Dirección no encontrada"
                )

                if (_origenFieldSelected.value == true) {
                    // Actualizar la información de la ubicación de origen
                    _originLocation.value = updatedLocationInfo
                } else if (_destinoFieldSelected.value == true) {
                    // Actualizar la información de la ubicación de destino
                    _destinationLocation.value = updatedLocationInfo
                }
            } catch (e: Exception) {
                Log.e("LocationViewModel", "Error al obtener dirección: ${e.message}")
            }
        }
    }

    fun updateLocationAddresses() {
        viewModelScope.launch {
            try {
                // Obtener la dirección para startLocation
                val startAddress = geocoder.getFromLocation(
                    _startLocation.value.coordinates.latitude,
                    _startLocation.value.coordinates.longitude,
                    1
                )
                val newStartLocation = _startLocation.value.copy(
                    address = startAddress?.get(0)?.getAddressLine(0) ?: "Dirección no encontrada"
                )
                _startLocation.value = newStartLocation

                // Obtener la dirección para endLocation
                val endAddress = geocoder.getFromLocation(
                    _endLocation.value.coordinates.latitude,
                    _endLocation.value.coordinates.longitude,
                    1
                )
                val newEndLocation = _endLocation.value.copy(
                    address = endAddress?.get(0)?.getAddressLine(0) ?: "Dirección no encontrada"
                )
                _endLocation.value = newEndLocation

            } catch (e: Exception) {
                Log.e("LocationViewModel", "Error al obtener dirección: ${e.message}")
            }
        }
    }


    fun setMapMarkerLocation(currentLocation: LatLng) {
        if (_destinoFieldSelected.value!!)
            _endLocation.value = _endLocation.value.copy(coordinates = currentLocation)

        if (_origenFieldSelected.value!!)
            _startLocation.value = _startLocation.value.copy(coordinates = currentLocation)
    }

    // Actualiza el estado de ubicación basado en si es un destino o un origen
    fun updateLocationState(isDestino: Boolean, addressState: AddressState) {
        if (isDestino) {
            destinationLocationState.value = addressState
            destinoAddressText = addressState.address
        } else {
            originLocationState.value = addressState
        }
    }


    fun getAddress(coords: LatLng) {
        viewModelScope.launch {
            try {
                val response = photonService.getAdressByLocation(
                    coords.latitude,
                    coords.longitude
                ) // Asegurar orden correcto

                if (response.isSuccessful) {
                    val body = response.body()

                    if (body != null) {

                        val feature =
                            body.features.firstOrNull() // Puede ser null si la API no encuentra nada
                        Log.d("getAddress", "Respuesta de la API: $feature")

                        if (feature != null) {
                            val address = feature.properties.toDomain()
                            Log.d("getAddress", "Dirección obtenida: $address")
                            _currentAddress.value = address
                        } else {
                            Log.e("getAddress", "Error: No se encontró una dirección válida")
//                            _currentAddress.value = "Dirección no encontrada"
                        }
                    } else {
                        Log.e("getAddress", "Error: Respuesta de la API es null")
//                        _currentAddress.value = "Error al obtener la dirección"
                    }
                } else {
                    Log.e(
                        "getAddress",
                        "Error en la API. Código: ${response.code()} - ${response.message()}"
                    )
//                    _currentAddress.value = "No se pudo obtener la dirección"
                }
            } catch (e: IOException) { // Error de red
                Log.e("getAddress", "Error de red: ${e.message}", e)
//                _currentAddress.value = "Error de conexión"
            } catch (e: HttpException) { // Error HTTP
                Log.e("getAddress", "Error HTTP: ${e.message}", e)
//                _currentAddress.value = "Error en el servidor"
            } catch (e: Exception) { // Otros errores inesperados
                Log.e("getAddress", "Excepción inesperada: ${e.message}", e)
//                _currentAddress.value = "Error desconocido"
            }
        }
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
            var currentLocation = LocationInfo(
                address = getAddressByLatLng(lastKnowedCoordinates),
                coordinates = lastKnowedCoordinates
            )
            if (_destinoFieldSelected.value!!)
                _endLocation.value = currentLocation

            if (_origenFieldSelected.value!!)
                _startLocation.value = currentLocation


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
                val address = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1)
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


    fun getAddressOrigen(latLng: LatLng) {
        viewModelScope.launch {
            val address = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1)
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

}
