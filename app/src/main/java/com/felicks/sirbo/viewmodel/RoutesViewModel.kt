package com.felicks.sirbo.viewmodel

import android.content.Context
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.felicks.sirbo.data.local.dao.PatternDao
import com.felicks.sirbo.data.local.dao.PatternGeometryDao
import com.felicks.sirbo.data.local.dao.RutasDao
import com.felicks.sirbo.data.local.dao.patternDetailDao.PatternDetailDao
import com.felicks.sirbo.data.local.dao.patternDetailDao.StopDao
import com.felicks.sirbo.data.local.dao.patternDetailDao.TripDao
import com.felicks.sirbo.data.local.entity.RutaEntity
import com.felicks.sirbo.data.local.entity.patternDetail.toDomain
import com.felicks.sirbo.data.local.entity.toDomain
import com.felicks.sirbo.data.local.entity.toDomainList
import com.felicks.sirbo.data.models.AddressState
import com.felicks.sirbo.data.models.Ruta
import com.felicks.sirbo.data.models.RutaState
import com.felicks.sirbo.data.models.otpModels.PatterDetail
import com.felicks.sirbo.data.models.otpModels.Pattern
import com.felicks.sirbo.data.models.otpModels.RouteStopItem
import com.felicks.sirbo.data.models.otpModels.routes.PatternGeometry
import com.felicks.sirbo.data.models.otpModels.routes.RutasItem
import com.felicks.sirbo.data.models.otpModels.routes.toEntity
import com.felicks.sirbo.data.models.otpModels.routes.toEntityList
import com.felicks.sirbo.data.models.otpModels.routing.Leg
import com.felicks.sirbo.data.models.otpModels.toEntity
import com.felicks.sirbo.data.models.otpModels.toEntityList
import com.felicks.sirbo.domain.models.SyncStatus
import com.felicks.sirbo.domain.repository.PlanRespository
import com.felicks.sirbo.utils.MapConfig
import com.felicks.sirbo.utils.NetworkUtils
import com.felicks.sirbo.utils.StringUtils.toApiString
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import java.net.SocketTimeoutException
import javax.inject.Inject

@HiltViewModel
class RoutesViewModel @Inject constructor(
//    private val otpService: OtpService
    private val planRepository: PlanRespository,
    private val rutasDao: RutasDao,
    private val patternDao: PatternDao,
    private val geometryDao: PatternGeometryDao,
    private val patternDetailDao: PatternDetailDao,
    private val stopsDao: StopDao,
    private val tripsDao: TripDao,
    @ApplicationContext private val context: Context,
) : ViewModel() {

    private val TAG = "RoutesViewModel";
    var state by mutableStateOf(RutaState())
        private set

    private val _allRoutesList =
        MutableStateFlow<List<com.felicks.sirbo.data.models.otpModels.routes.RutasItem>>(emptyList())
    val allRoutesList: StateFlow<List<com.felicks.sirbo.data.models.otpModels.routes.RutasItem>> =
        _allRoutesList

    private val _filteredRoutesList =
        MutableStateFlow<List<com.felicks.sirbo.data.models.otpModels.routes.RutasItem>>(emptyList())
    val filteredRoutesList: StateFlow<List<com.felicks.sirbo.data.models.otpModels.routes.RutasItem>> =
        _filteredRoutesList

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _selectedPatternGeometry = MutableStateFlow(PatternGeometry())
    val selectedPatternGeometry: StateFlow<PatternGeometry> = _selectedPatternGeometry.asStateFlow()

    private val _isSyncing: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isSyncing: StateFlow<Boolean> = _isSyncing

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

    private val _errorState = MutableStateFlow<String?>(null)
    val errorState: StateFlow<String?> = _errorState.asStateFlow()


    // Function to update data
    fun updateErrorState(newData: String?) {
        _errorState.value = newData
    }

    private val _routeSelected =
        MutableStateFlow<com.felicks.sirbo.data.models.otpModels.routes.RutasItem>(
            RutasItem()
        )
    val routeSelected: MutableStateFlow<com.felicks.sirbo.data.models.otpModels.routes.RutasItem> =
        _routeSelected

    private val _errorToastMessage = MutableStateFlow<String?>(null) // Estado para manejar errores
    val errorToastMessage: MutableStateFlow<String?> = _errorToastMessage

    var routePatterns by mutableStateOf(listOf<Pattern>())
        private set

    var _routeSelectePattern = MutableStateFlow<PatterDetail>(PatterDetail())
    var routeSelectePattern: MutableStateFlow<PatterDetail> = _routeSelectePattern

    var optimalRouteLegs by mutableStateOf<List<Leg>>(emptyList())
        private set

    private val _isLocalLoading = MutableStateFlow<Boolean>(false) // Estado para manejar errores
    val isLoading: MutableStateFlow<Boolean> = _isLocalLoading

    private val _syncStatus = MutableStateFlow<SyncStatus>(SyncStatus.CARGANDO)
    val syncStatus: StateFlow<SyncStatus> = _syncStatus


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

    fun setRouteSelected(rutasItem: com.felicks.sirbo.data.models.otpModels.routes.RutasItem) {
        _routeSelected.value = rutasItem
    }

    suspend fun insertarRutas(rutas: List<RutaEntity>): Boolean = withContext(Dispatchers.IO) {
        return@withContext try {
            val resultado = rutasDao.insertAll(rutas)
            resultado.all { it != -1L }
        } catch (e: Exception) {
            Log.e("RoutesViewModel", "Ocurrió un error de inserción: ${e.message}")
            false
        }
    }

    fun cargarRutasLocales() {
        viewModelScope.launch {
            _syncStatus.value = SyncStatus.CARGANDO
            val rutasLocales = rutasDao.getAllRoutes().toDomainList()
            val rutasOrdenadas = rutasLocales.sortedBy { it.shortName.lowercase() }
            _filteredRoutesList.value = rutasOrdenadas
//            _syncStatus.value = null
        }
    }

    fun sincronizarRutasRemotas() {
        viewModelScope.launch {
            if (!NetworkUtils.isInternetAvailable(context)) {
                Log.w(TAG, "Sin conexión, no se sincroniza")
                return@launch
            }

            try {
                _isSyncing.value = true
                _syncStatus.value = SyncStatus.CARGANDO

                val resultado = withContext(Dispatchers.IO) {
                    planRepository.fetchRoutes()
                }

                val rutasRemotas = resultado.body() ?: emptyList()

                val rutasOrdenadas = rutasRemotas.sortedBy { it.shortName.lowercase() }
                _allRoutesList.value = rutasOrdenadas
                _filteredRoutesList.value = rutasOrdenadas

                // Variantes, geometrías y detalles
                for (item in rutasRemotas) {
                    val resultadoVariantes = planRepository.getPatternByRouteId(item.id)
                    if (!resultadoVariantes.isSuccessful) continue

                    val variantes = resultadoVariantes.body() ?: emptyList()
                    patternDao.insertAll(variantes.toEntityList(item.id))

                    for (variante in variantes) {
                        val geometriaVariante = fetchRouteGeometry(variante.id)
                        geometryDao.insert(geometriaVariante.toEntity(variante.id))

                        val responseVariante =
                            planRepository.getPatternDetailsByPatternId(variante.id)
                        val detalleVariante = responseVariante.body() ?: continue
                        patternDetailDao.insert(detalleVariante.toEntity())
                        guardarPatternDetail(detalleVariante)
                    }
                }
            } catch (e: SocketTimeoutException) {
                Log.e(TAG, "Timeout de conexión: ${e.message}")
                _errorToastMessage.value = "No se pudo conectar al servidor. "
            } catch (e: Exception) {
                Log.e(TAG, "Error inesperado", e)
//                _syncStatus.value = SyncStatus.ERROR_GENERAL
                _errorToastMessage.value = "Ocurrió un error inesperado"
            } finally {
//                _syncStatus.value = null
                _isSyncing.value = false
            }
        }
    }

    fun syncRutasSiEsNecesario() {
        cargarRutasLocales()
        sincronizarRutasRemotas()
    }

    fun obtenerRutas() {
        viewModelScope.launch {
            _isLocalLoading.value = true
            // 1. Mostrar datos locales inmediatamente
            val rutasLocales = rutasDao.getAllRoutes().toDomainList()
            val rutasOrdenadas = rutasLocales.sortedBy { it.shortName.lowercase() }
            Log.d(TAG, "Rutas locales obtenidas ${rutasLocales.size}")
            _isLocalLoading.value = false

            _filteredRoutesList.value = rutasOrdenadas
            _syncStatus.value = SyncStatus.CARGANDO
            // ← Mostrar indicador visual en UI (gris)
            // 2. Intentar sincronizar en segundo plano
            try {
                _isSyncing.value = true
                if (NetworkUtils.isInternetAvailable(context)) {
                    return@launch
                }
                val resultado = withContext(Dispatchers.IO) {
//                    _syncStatus.value = SyncStatus.SINCRONIZANDO // ← Mostrar in
                    planRepository.fetchRoutes()
                }
                val rutasRemotas = resultado.body() ?: emptyList()

                if (rutasRemotas.isNotEmpty()) {
                    val exito = insertarRutas(rutasRemotas.toEntityList())
                    if (exito) {
                        // Acutalizar UI

                        val rutasActualizadas = rutasRemotas.sortedBy { it.shortName.lowercase() }
                        _allRoutesList.value = rutasActualizadas
                        _filteredRoutesList.value = rutasActualizadas
                        // obtener
                        for (item in rutasActualizadas) {
                            val resultadoVariantes = planRepository.getPatternByRouteId(item.id)
                            if (resultadoVariantes.isSuccessful) {
                                val variantes = resultadoVariantes.body() ?: emptyList()
                                patternDao.insertAll(variantes.toEntityList(item.id))
                                for (variante in variantes) {
                                    val geometriaVariante = fetchRouteGeometry(variante.id)
                                    geometryDao.insert(geometriaVariante.toEntity(variante.id))
                                    val responseVariante =
                                        planRepository.getPatternDetailsByPatternId(variante.id)
                                    val detalleVariante = responseVariante.body() ?: PatterDetail()
                                    patternDetailDao.insert(detalleVariante.toEntity())
                                    guardarPatternDetail(detalleVariante)

                                }
                            } else {
//                                return@launch
                                continue
                            }
                        }

                    } else {
//                        _syncStatus.value =
//                            SyncStatus.ERROR_INSERCION // ← Mostrar advertencia o ícono de error
                    }
                    Log.d("RoutesViewModel", "Sincronización completada con éxito")
                } else {
//                    _syncStatus.value = SyncStatus.VACIO_REMOTO
                }

            } catch (e: SocketTimeoutException) {
                Log.e("RutasViewModel", "Error de conexión: ${e.message}")
//                _syncStatus.value = SyncStatus.ERROR_CONEXION
                _errorToastMessage.value = "No se pudo conectar con el servidor. Verifica la red."

            } catch (e: Exception) {
                Log.e("RutasViewModel", "Error al obtener las rutas", e)
                _errorToastMessage.value =
                    "Ocurrió un error inesperado" // cambiarpor un itempo de erro personalzaod
            } finally {
                _isSyncing.value = false
            }
        }
    }

    suspend fun guardarPatternDetail(detail: PatterDetail) {
        stopsDao.insertAll(detail.stops.toEntityList(detail.id))
        tripsDao.insertAll(detail.trips.toEntityList(detail.id))
    }

    fun getRouteGeometry(patternId: String) {
        viewModelScope.launch {
            try {
                val resultado = planRepository.getGeomByPattern("$patternId::01")
                _selectedPatternGeometry.value = resultado.body() ?: PatternGeometry()
            } catch (e: Exception) {
//                Actualizar error state
            }
        }
    }

    fun getVariante(rutaId: String, position: Int) {
        viewModelScope.launch {
            try {
                val detalle = withContext(Dispatchers.IO) {
                    val variantes = patternDao.getPatternsByRutaId(rutaId).toDomainList()
                    val varianteSeleccionada = variantes[position]
                    val stops = stopsDao.getStopsByPatternId(varianteSeleccionada.id)
                    val trips = tripsDao.getTripsByPatternId(varianteSeleccionada.id)
                    val detalleVariante = patternDetailDao.getById(varianteSeleccionada.id)
                    val geometria = geometryDao.getByPatternId(varianteSeleccionada.id)
                    val detalleRuta = rutasDao.getRouteById(rutaId)
                    _routeSelected.value = detalleRuta!!.toDomain()
                    _selectedPatternGeometry.value = geometria!!.toDomain()
                    detalleVariante?.toDomain(stops, trips)
                }

                detalle?.let {
                    _routeSelectePattern.value = it
                } ?: Log.w("PlannerViewModel", "Detalle variante no encontrado")

            } catch (e: Exception) {
                Log.e(TAG, "Error atrapado en routesViewMOdel: ${e.message}")
            }
        }
    }

    fun getVarianteDebug(rutaId: String, position: Int) {
        runBlocking {
            val variantes = patternDao.getPatternsByRutaId(rutaId).toDomainList()
            val varianteSeleccionada = variantes[position]
            val stops = stopsDao.getStopsByPatternId(varianteSeleccionada.id)
            val trips = tripsDao.getTripsByPatternId(varianteSeleccionada.id)
            val detalleVariante = patternDetailDao.getById(varianteSeleccionada.id)

            val detalle = detalleVariante?.toDomain(stops, trips)
            println("Detalle = $detalle")
        }
    }


    suspend fun fetchRouteGeometry(patternId: String): PatternGeometry {
        return withContext(Dispatchers.IO) {
            val response = planRepository.getGeomByPattern(patternId)
            response.body() ?: PatternGeometry()
        }
    }

    fun getRouteDetails(id: String) {
        viewModelScope.launch {
            try {
//                val resultado = otpService.getRouteDetail(id)
                val resultado = planRepository.getRouteDetail(id)
                _routeSelected.value = resultado
                Log.d("RutasViewModel", "Detalle de ruta" + resultado)
            } catch (e: Exception) {
                // Manejar errores, por ejemplo, emitir un estado de error
                Log.e("RutasViewModel", "Error al Obtner detalle de ruta", e)
            }
        }
    }

    suspend fun getVariantesPorRutaID(id: String): List<Pattern> {
        return withContext(Dispatchers.IO) {
            patternDao.getPatternsByRutaId(id).toDomainList()
            // ahora se obtiene la geometría de un variante especifica
        }
    }

    fun getGeometriaDeVariante(idRuta: String, posicionVariante: Int) {
        viewModelScope.launch() {
            val variantes = getVariantesPorRutaID(idRuta)

        }
    }

    fun getGeometriaByRutaID(id: String) {
        viewModelScope.launch() {
            val variantesDeRuta: List<Pattern> = patternDao.getPatternsByRutaId(id).toDomainList()
            // ahora se obtiene la geometría de un variante especifica

        }
    }

    fun getRouteStops(id: String) {
        Log.d("RutasViewModel", "Pasando id ruta" + id)
        viewModelScope.launch {
            try {
//                val resultado = otpService.getPatternByRouteId(id)
                val resultado = planRepository.getPatternByRouteId(id)
                Log.d("RutasViewModel", "primer rqe enviado es " + resultado)
                if (resultado.isSuccessful) {
                    routePatterns = resultado.body() ?: emptyList()
                    Log.d("RutasViewModel", "paradas o obtenidas $routePatterns")
//                    var patternRespose = otpService.getPatternDetailsByPatternId(routePatterns[0].id)
                    var patternRespose =
                        planRepository.getPatternDetailsByPatternId(routePatterns[0].id)
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

    @Deprecated("A remover usar itinerarios en su lugar")
    fun getOptimalRoutes(fromLocation: AddressState, toLocation: AddressState) {
        viewModelScope.launch {
            try {
//                val resultado = otpService.getOptimalRoutes(
//                    fromLocation.coordinates.toApiString(), toLocation.coordinates.toApiString()
//                )
                val resultado = planRepository.getOptimalRoutes(
                    fromLocation.coordinates.toApiString(), toLocation.coordinates.toApiString()
                )
                Log.d("RutasViewModel", "resultado de la ruta optima es " + resultado.body())
//            TODO:
//             realiza la validacion en cuaso de existe el objeto error en lugar de otro de los itinerarios
                val rutas = resultado.body()?.plan!!.itineraries?.get(0)?.legs
                optimalRouteLegs = resultado.body()?.plan!!.itineraries?.get(0)!!.legs
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
