package com.felicksdev.onlymap

import android.Manifest
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.felicksdev.onlymap.databinding.ActivityMain2Binding
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity2 : AppCompatActivity()  {
    private lateinit var binding: ActivityMain2Binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityMain2Binding.inflate(layoutInflater)
        setContentView(binding.root)
        /*replaceFragment(Home())
        binding.bottomNavigationView.setOnItemSelectedListener {
            when (it.itemId){
                R.id.home_fragment ->replaceFragment(Home())
                R.id.addresses_fragment ->replaceFragment(Addresses())
                R.id.routes_fragment ->replaceFragment(Routes())
                else->{
                }
            }
            true
        }*/
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        val navController = findNavController(R.id.nav_host_fragment_container)
        //binding.bottomNavigationView.setupWithNavController(navController)
        bottomNavigationView.setupWithNavController(navController)
        //createFragment()
        checkSelfPermission()
        //val retrofitTraer = consumirAPI.getRutas()
        //Log.d("Retrofit",   retrofitTraer.toString())

        //mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        //mapFragment.getMapAsync(this) // Asegúrate de llamar a getMapAsync para activar onMapReady
    }

    private fun checkSelfPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)!=PackageManager.PERMISSION_GRANTED){
            requestLocationPermission()
        }else{
            //Abrir camara
            Toast.makeText(this,"Permisos de ubicacion concedidos",Toast.LENGTH_SHORT).show()
        }
    }

    private fun requestLocationPermission() {
        if(ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.ACCESS_COARSE_LOCATION)){
            //Usuario rechazó permisos
            Toast.makeText(this,"Permisos de ubicacion rechazador por segunda vez",Toast.LENGTH_SHORT).show()
        }else{
            // pedir permisos
            ActivityCompat.requestPermissions(this , arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION),777)
        }
    }
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode==777){
            if (grantResults.isNotEmpty()&& grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Toast.makeText(this,"Permisos de ubicacion concedidos",Toast.LENGTH_SHORT).show()
            }else{
                //
                Toast.makeText(this,"Permisos rechazdo por primera vez",Toast.LENGTH_SHORT).show()
            }
        }
    }







}