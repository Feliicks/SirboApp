package com.felicksdev.onlymap

import RutasViewModel
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.felicksdev.onlymap.data.network.MyApiService
import com.felicksdev.onlymap.data.models.Ruta
import com.felicksdev.onlymap.ui.theme.OnlyMapTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [Routes.newInstance] factory method to
 * create an instance of this fragment.
 */
class Routes : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var routesList: List<Ruta> = emptyList()
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
        val view = inflater.inflate(R.layout.fragment_routes, container, false)
        val composeView = view.findViewById<ComposeView>(R.id.routesComposeView)

        val navController = findNavController()
        val rutasViewModel = viewModels<RutasViewModel> ()
        // Obtén la ruta actual
        //val currentDestination = navController.currentDestination
        //val currentRoute = currentDestination?.label.toString()

        // Haz algo con la ruta, por ejemplo, imprímela
        //Log.d("TuFragment", "Ruta actual: $currentRoute")

        getRoutes()
        composeView.setContent {
            OnlyMapTheme {
                // Utiliza RoutesScreen() en lugar de Routes()

                //RoutesScreen(rutasViewModel, navController)
                //RoutesList(viewModel)
            }
        }
        return view
    }

    private fun getRoutes(nombre: String = "893") {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                //val call = getRetrofit().create(MyApiService::class.java).getRutas()
                val call = getRetrofit().create(MyApiService::class.java).getLinea("ruta/nombre/${nombre}")
                val rutas: List<Ruta>? = call.body()
                if (call.isSuccessful && !rutas.isNullOrEmpty()) {
                    // Asigna la lista de rutas a la variable
                    routesList = rutas
                } else {
                    Log.d("Retrofit", call.errorBody().toString())
                }
            } catch (e: Exception) {
                Log.e("Error", "Error al obtener rutas: ${e.message}")
            }
        }
    }
    private fun getRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("http://10.0.2.2:3000/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
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
                        //printRoute(rutas)
                        //Alimnetar las vista delc mponte RouteItemCard


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
    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment Routes.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            Routes().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}