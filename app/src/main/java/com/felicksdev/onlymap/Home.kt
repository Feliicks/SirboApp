package com.felicksdev.onlymap

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.felicksdev.onlymap.data.models.GeometriaRuta
import com.felicksdev.onlymap.data.network.MyApiService
import com.felicksdev.onlymap.data.models.Ruta
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.GoogleMap.OnMyLocationButtonClickListener
import com.google.android.gms.maps.GoogleMap.OnMyLocationClickListener
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import com.google.maps.android.data.geojson.GeoJsonLayer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [Home.newInstance] factory method to
 * create an instance of this fragment.
 */
class Home : Fragment(),
    OnMyLocationButtonClickListener,
    OnMyLocationClickListener,
    OnMapReadyCallback {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var mMap: GoogleMap
    private var mapFragment: SupportMapFragment? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_home, container, false)
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment?.getMapAsync(this)
        return view

    }

    private fun createMarker() {

        val favoritePlace = LatLng(
            -34.0, 151.0
        )
        val laPazCordinates = LatLng(
            -16.5207124, -68.1240775
        )
        mMap.addMarker(
            MarkerOptions().position(laPazCordinates).title("Mi favorite palce")
        )
        mMap.animateCamera(
            CameraUpdateFactory.newLatLng(laPazCordinates),
            4000,
            null
        )
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment Home.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            Home().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }



    override fun onMapReady(googleMap: GoogleMap) {
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
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

        //Toast.makeText(context, "Soy el texto", Toast.LENGTH_LONG).show()
        mMap = googleMap
        //createMarker()
        mMap.isMyLocationEnabled = true
        mMap.setOnMyLocationButtonClickListener(this)
        mMap.setOnMyLocationClickListener(this)

        // Crear la capa GeoJsonLayer
        //loadGeoJson()
        //retrieveFileFromResource()
        //val retrofitTraer = consumirAPI.getRutas()x
        //Log.d("Retrofit",   retrofitTraer.toString())
        //searchByRuta();
        //getRouteByLinea("893")
        //mostrarRutaEnMapa(522)
        //getRouteById(522)
    }

    private fun retrieveFileFromResource() {
        try {
            val layer = GeoJsonLayer(mMap, R.raw.ruta, context)
            layer.addLayerToMap()
        } catch (e: IOException) {
            Log.e("ERROR", "GeoJSON file could not be read")
        } catch (e: JSONException) {
            Log.e("ERROR", "GeoJSON file could not be converted to a JSONObject")
        }
    }
    private fun getRouteByLinea(Linea: String) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val call = getRetrofit().create(MyApiService::class.java).getLinea("ruta/nombre/$Linea")
                val rutas: List<Ruta>? = call.body()

                if (call.isSuccessful && !rutas.isNullOrEmpty()) {
                    // Procesar y mostrar las rutas en el mapa
                    withContext(Dispatchers.Main) {
                        //searchByLineas2(rutas)
                        printRoute(rutas)
                    }
                } else {
                    Log.d("Retrofit", call.errorBody().toString())
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Log.e("Error", "Error al obtener y mostrar rutas: ${e.message}")
                    //Toast.makeText(context, "Ocurrio un error al obtener las rutas: ${e.message}", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun loadGeoJson() {
        //val geoJson = GeoJsonLoader.loadGeoJson(this, R.raw.ruta)
        //val layer = GeoJsonLayer(mMap, R.raw.ruta)
        //layer.addLayerToMap();
    }

    private fun getRetrofit(): Retrofit{
        return Retrofit.Builder()
            .baseUrl("http://10.0.2.2:3000/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    private fun mostrarRutaEnMapa(geometriaRuta: GeometriaRuta) {
        try {
            val geoJsonString = convertirGeometriaARawGeoJson(geometriaRuta)
            val layer = GeoJsonLayer(mMap, JSONObject(geoJsonString))
            layer.addLayerToMap()
        } catch (e: JSONException) {
            Log.e("Error", "Error al procesar GeoJSON: ${e.message}")
        }
    }
    private fun convertirGeometriaARawGeoJson(geometriaRuta: GeometriaRuta): String {
        // Aquí debes implementar la lógica para convertir la geometría de la ruta a GeoJSON
        // Puedes usar la librería Gson u otras para hacerlo
        // Por ahora, supongamos que la geometría ya está en formato GeoJSON
        return geometriaRuta.geoJsonString
    }
    private fun searchByLinea(){
        CoroutineScope(Dispatchers.IO).launch {
            val call = getRetrofit().create(MyApiService::class.java).getLinea("ruta/nombre/893")
            val rutaVehicular = call.body()
            if (call.isSuccessful){
                //Procesar para verificar que no existe error
                Log.d("Retrofit", rutaVehicular.toString())
                //val geometriaRuta = rutas?.geometria_ruta
                //val geoJson = JSONObject(geometriaRuta.getTuMetodoParaObtenerLaGeometria())

            }else{
                Log.d("Retrofit", call.errorBody().toString())
            }
        }
    }
    private fun getRouteById(rutaId: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val call = getRetrofit().create(MyApiService::class.java).getRuta("ruta/$rutaId")
                val ruta: Ruta? = call.body()
                if (ruta != null) {
                    withContext(Dispatchers.Main) {
                        // Limpiar cualquier polilínea existente en el mapa antes de agregar la nueva
                        mMap.clear()
                        // Mostrar la ruta en el mapa
                        printRoute(ruta)
                    }
                }
            } catch (e: Exception) {
                //mostrar Toas y no actualizar
                Log.e("Error", "Error al obtener la ruta: ${e.message}")
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, "Ocurrio un error al obtener la ruta: ${e.message}", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun printRoute(ruta: Ruta) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                withContext(Dispatchers.Main) {
                    // Limpiar cualquier polilínea existente en el mapa antes de agregar la nueva
                    // With contexto vuelve a trabajr en el hilo principal
                    mMap.clear()
                }
                val polylineOptions = PolylineOptions()

                ruta.geometria_ruta.coordinates.forEach { coordinates ->
                    coordinates.forEach { coordinate ->
                        val latLng = LatLng(coordinate[1], coordinate[0])
                        polylineOptions.add(latLng)
                    }
                }

                val color = when (ruta.sentido_ruta.sentido) {
                    "i" -> Color.BLUE
                    "v" -> Color.RED
                    "c" -> Color.GREEN
                    // Agrega más casos según sea necesario
                    else -> Color.BLACK
                }

                withContext(Dispatchers.Main) {
                    // Asignar propiedades al PolylineOptions fuera del bucle para evitar duplicaciones
                    polylineOptions.color(color)
                    polylineOptions.width(5f) // Ancho de la polilínea (ajusta según tus necesidades)

                    // Agregar la polilínea al mapa una sola vez fuera del bucle
                    mMap.addPolyline(polylineOptions)

                    // Centrar el mapa en la geometría de la ruta
                    mMap.moveCamera(
                        CameraUpdateFactory.newLatLngBounds(
                            getLatLngBounds(ruta.geometria_ruta.coordinates),
                            10
                        )
                    )
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, "Ocurrio un error al obtener la ruta: ${e.message}", Toast.LENGTH_LONG).show()
                }
                Log.e("Error", "Error al obtener la ruta: ${e.message}")
            }
        }
    }

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

    private fun searchByLineas2(rutas: List<Ruta>?) {
        val colores = arrayOf(Color.BLUE, Color.RED) // Puedes agregar más colores según sea necesario
        rutas?.forEachIndexed { index, ruta ->
            val polylineOptions = PolylineOptions()
            Log.d("rutasGet", rutas[index].toString())
            ruta.geometria_ruta.coordinates.forEach { coordinates ->
                coordinates.forEach { coordinate ->
                    val latLng = LatLng(coordinate[1], coordinate[0])
                    polylineOptions.add(latLng)
                }
            }

            // Asignar colores diferentes según el tipo de ruta
//            val color = when (ruta.tipo_ruta) {
//                TipoRuta.IDA -> Color.BLUE
//                TipoRuta.VUELTA -> Color.RED
//                // Puedes agregar más casos según sea necesario
//            }

            //polylineOptions.color(color)
            //mMap.addPolyline(polylineOptions)

            mMap.addPolyline(polylineOptions)
        }

        // Centra el mapa en una posición inicial (reemplazar con tus coordenadas iniciales)
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(0.0, 0.0), 12f))
    }
    private fun printRoute (rutas: List<Ruta>){
        CoroutineScope(Dispatchers.IO).launch {
            try {
                withContext(Dispatchers.Main) {
                    // Limpiar cualquier polilínea existente en el mapa antes de agregar las nuevas
                    mMap.clear()
                }
                for (ruta in rutas) {
                    val polylineOptions = PolylineOptions()

                    ruta.geometria_ruta.coordinates.forEach { coordinates ->
                        coordinates.forEach { coordinate ->
                            val latLng = LatLng(coordinate[1], coordinate[0])
                            polylineOptions.add(latLng)
                        }
                    }

                    val color = when (ruta.sentido_ruta.sentido) {
                        "i" -> Color.BLUE
                        "v" -> Color.RED
                        "c" -> Color.GREEN
                        // Agrega más casos según sea necesario
                        else -> Color.BLACK
                    }

                    withContext(Dispatchers.Main) {
                        // Asignar propiedades al PolylineOptions fuera del bucle para evitar duplicaciones
                        polylineOptions.color(color)
                        polylineOptions.width(5f) // Ancho de la polilínea (ajusta según tus necesidades)

                        // Agregar la polilínea al mapa una sola vez fuera del bucle
                        mMap.addPolyline(polylineOptions)
                    }
                }

                // Centrar el mapa en la geometría de la primera ruta (ajusta según tus necesidades)
                if (rutas.isNotEmpty()) {
                    withContext(Dispatchers.Main) {
                        mMap.moveCamera(
                            CameraUpdateFactory.newLatLngBounds(
                                getLatLngBounds(rutas[0].geometria_ruta.coordinates),
                                10
                            )
                        )
                    }
                }
            } catch (e: Exception) {
                Log.e("Error", "Error al obtener las rutas: ${e.message}")
            }
        }
    }
    private fun createGeoJsonFromGeometriaRuta(geometriaRuta: GeometriaRuta?): JSONObject {
        val geoJson = JSONObject()
        try {
            geoJson.put("type", geometriaRuta?.type)
            geoJson.put("coordinates", JSONArray(geometriaRuta?.coordinates))
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        return geoJson
    }
    private fun searchByRuta(){
        CoroutineScope(Dispatchers.IO).launch {
            val call = getRetrofit().create(MyApiService::class.java).getRuta("ruta/522")
            val ruta: Ruta? = call.body()
            if (call.isSuccessful){
                //Procesar para verificar que no existe error
                val geoJson = createGeoJsonFromGeometriaRuta(ruta?.geometria_ruta)
                withContext(Dispatchers.Main) {
                    // Añadir la capa GeoJsonLayer al mapa
                    val geoJsonLayer = GeoJsonLayer(mMap, geoJson)
                    geoJsonLayer.addLayerToMap()

                    // Verificar si la capa tiene bounding box antes de intentar centrar el mapa
                    if (geoJsonLayer.boundingBox != null) {
                        // Centrar el mapa en la geometría de la ruta
                        mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(geoJsonLayer.boundingBox, 10))
                    } else {
                        // Manejar el caso en que la capa no tiene bounding box
                        Log.e("MapError", "La capa GeoJsonLayer no tiene bounding box definido")
                        // Obtener el primer punto de la geometría y centrar el mapa en él
                        val     primerPunto = LatLng(geoJson.getJSONArray("coordinates").getJSONArray(0).getJSONArray(0).getDouble(1),
                            geoJson.getJSONArray("coordinates").getJSONArray(0).getJSONArray(0).getDouble(0))
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(primerPunto, 10f))
                    }
                }
            }else{
                Log.d("Retrofit", call.errorBody().toString())
            }
        }
    }

    override fun onMyLocationButtonClick(): Boolean {
        Toast.makeText(context, "MyLocation button clicked", Toast.LENGTH_SHORT)
            .show()
        // Return false so that we don't consume the event and the default behavior still occurs
        // (the camera animates to the user's current position).
        return false
    }

    override fun onMyLocationClick(location: Location) {
        Toast.makeText(context, "Current location:\n$location", Toast.LENGTH_LONG)
            .show()
    }

}