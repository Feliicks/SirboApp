package com.felicksdev.onlymap

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.felicksdev.onlymap.models.GeometriaRuta
import com.felicksdev.onlymap.models.MyApiService
import com.felicksdev.onlymap.models.Ruta
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.GoogleMap.OnMyLocationButtonClickListener
import com.google.android.gms.maps.GoogleMap.OnMyLocationClickListener
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
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
        searchByRuta();
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
                        val primerPunto = LatLng(geoJson.getJSONArray("coordinates").getJSONArray(0).getJSONArray(0).getDouble(1),
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