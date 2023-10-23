package com.felicksdev.onlymap

import android.Manifest
import android.content.ContentValues
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import com.mapbox.android.gestures.MoveGestureDetector
import com.mapbox.maps.CameraOptions
import com.mapbox.maps.MapView
import com.mapbox.maps.Style
import com.mapbox.maps.extension.style.expressions.dsl.generated.interpolate
import com.mapbox.maps.plugin.LocationPuck2D
import com.mapbox.maps.plugin.gestures.OnMoveListener
import com.mapbox.maps.plugin.gestures.gestures
import com.mapbox.maps.plugin.locationcomponent.OnIndicatorBearingChangedListener
import com.mapbox.maps.plugin.locationcomponent.OnIndicatorPositionChangedListener
import com.mapbox.maps.plugin.locationcomponent.location
import com.felicksdev.onlymap.utils.LocationPermissionHelper
import android.location.Location
import android.location.LocationManager
import android.os.Looper
import androidx.annotation.DrawableRes
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.*
import com.mapbox.geojson.Point
import com.mapbox.maps.plugin.annotation.annotations
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationOptions
import com.mapbox.maps.plugin.annotation.generated.createPointAnnotationManager
import com.mapbox.turf.TurfMeasurement
import java.lang.ref.WeakReference


/**
 * Tracks the user location on screen, simulates a navigation session.
 */

class MainActivity : AppCompatActivity() {
    private lateinit var btnStartTrack:Button;
    private lateinit var btnStopTrack:Button;
    val MIN_DISTANCE_THRESHOLD = 1 // Definir la distancia mínima en metros
    var lastPoint: Point? = null

    //private lateinit var lastLocation: Location
    private val routeLocations = mutableListOf<Location?>()
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient;

    //Objeto con los datos para manupualr el Locatiojn
    private lateinit var locationRequest: LocationRequest

    // globally declare LocationCallback
    //Deuvelkve un LocationResult es como listener
    private lateinit var locationCallback: LocationCallback

    private val locationList = ArrayList<Location>()

    private lateinit var locationPermissionHelper: LocationPermissionHelper

    private val onIndicatorBearingChangedListener = OnIndicatorBearingChangedListener {
        //mapView.getMapboxMap().setCamera(CameraOptions.Builder().bearing(it).build())
    }

    private val onIndicatorPositionChangedListener = OnIndicatorPositionChangedListener {
        mapView.getMapboxMap().setCamera(CameraOptions.Builder().center(it).build())
        mapView.gestures.focalPoint = mapView.getMapboxMap().pixelForCoordinate(it)
    }

    private fun getLocationUpdate(){
        fusedLocationProviderClient= LocationServices.getFusedLocationProviderClient(this)
        locationRequest = LocationRequest.create().apply {
            // Sets the desired interval for active location updates. This interval is inexact. You
            // may not receive updates at all if no location sources are available, or you may
            // receive them less frequently than requested. You may also receive updates more
            // frequently than requested if other applications are requesting location at a more
            // frequent interval.
            //
            // IMPORTANT NOTE: Apps running on Android 8.0 and higher devices (regardless of
            // targetSdkVersion) may receive updates less frequently than this interval when the app
            // is no longer in the foreground.
            interval = 4000;//TimeUnit.SECONDS.toMillis(60)

            // Sets the fastest rate for active location updates. This interval is exact, and your
            // application will never receive updates more frequently than this value.
            fastestInterval =2000; //TimeUnit.SECONDS.toMillis(30)

            // Sets the maximum time when batched location updates are delivered. Updates may be
            // delivered sooner than this interval.
            //maxWaitTime = TimeUnit.MINUTES.toMillis(2)

            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }
    
        locationCallback=object : LocationCallback(){

            override fun onLocationResult(locationResult: LocationResult) {
                //Log.d("LOCATION","iNICIANDO ACUTALIZACIONES");
                super.onLocationResult(locationResult)

                // Normally, you want to save a new location to a database. We are simplifying
                // things a bit and just saving it as a local variable, as we only need it again
                // if a Notification is created (when the user navigates away from app).

                if (locationResult!=null && locationResult.lastLocation!=null ){
                    Log.d("LOCATION_UPDATE"," ${locationResult.lastLocation?.latitude} y ${locationResult.lastLocation?.longitude}")

                    addAnnotationToMap(
                        (locationResult.lastLocation?.longitude)!!.toDouble(),
                        (locationResult.lastLocation?.latitude)!!.toDouble()
                    );

                    //tvCurrentCordinates.text="Actual ${locationResult?.lastLocation?.latitude} y ${locationResult.lastLocation?.longitude}"


                }

                //currentLocation = locationResult.lastLocation
                // Notify our Activity that a new location was added. Again, if this was a
                // production app, the Activity would be listening for changes to a database
                // with new locations, but we are simplifying things a bit to focus on just
                // learning the location side of things.


                //val intent = Intent(ACTION_FOREGROUND_ONLY_LOCATION_BROADCAST)
                //intent.putExtra(TelecomManager.EXTRA_LOCATION, currentLocation)
                //LocalBroadcastManager.getInstance(applicationContext).sendBroadcast(intent)

                // Updates notification content if this service is running as a foreground
                // service.

            }
        }
    }
    private fun bitmapFromDrawableRes(context: Context, @DrawableRes resourceId: Int) =
        convertDrawableToBitmap(AppCompatResources.getDrawable(context, resourceId))

    private fun convertDrawableToBitmap(sourceDrawable: Drawable?): Bitmap? {
        if (sourceDrawable == null) {
            return null
        }
        return if (sourceDrawable is BitmapDrawable) {
            sourceDrawable.bitmap
        } else { 
// copying drawable object to not manipulate on the same reference
            val constantState = sourceDrawable.constantState ?: return null
            val drawable = constantState.newDrawable().mutate()
            val bitmap: Bitmap = Bitmap.createBitmap(
                drawable.intrinsicWidth, drawable.intrinsicHeight,
                Bitmap.Config.ARGB_8888
            )
            val canvas = Canvas(bitmap)
            drawable.setBounds(0, 0, canvas.width, canvas.height)
            drawable.draw(canvas)
            bitmap
        }
    }
    private fun addAnnotationToMap(longitude:Double ,latitude:Double) {
// Create an instance of the Annotation API and get the PointAnnotationManager.
        bitmapFromDrawableRes(
            this@MainActivity,
            R.drawable.red_marker
        )?.let {
            val annotationApi = mapView?.annotations
            val pointAnnotationManager = annotationApi?.createPointAnnotationManager(mapView!!)
// Set options for the resulting symbol layer.
            val pointAnnotationOptions: PointAnnotationOptions = PointAnnotationOptions()
// Define a geographic coordinate.
                .withPoint(Point.fromLngLat(longitude, latitude))
// Specify the bitmap you assigned to the point annotation
// The bitmap will be added to map style automatically.
                .withIconImage(it)
// Add the resulting pointAnnotation to the map.
            pointAnnotationManager?.create(pointAnnotationOptions)
        }
    }


    private val onMoveListener = object : OnMoveListener {
        override fun onMoveBegin(detector: MoveGestureDetector) {
            //Cuando empeixe gesto de mapas manda mesaje Toast
            //onCameraTrackingDismissed()
        }

        override fun onMove(detector: MoveGestureDetector): Boolean {
            return false
        }

        override fun onMoveEnd(detector: MoveGestureDetector) {}
    }
    private lateinit var mapView: MapView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //Inicializar variable de mapa con los datos
        mapView = MapView(this)
        //Mostrar el mapa después de inicialiar el diseño
        setContentView(R.layout.activity_main)
        //Dar referencia del mapa en el diseño des
        mapView = findViewById(R.id.mapView)


        //Carga estilo de mapas
        //mapView?.getMapboxMap()?.loadStyleUri(Style.MAPBOX_STREETS)

        //Solicitar permisos, verificar si tiene permisos
        locationPermissionHelper = LocationPermissionHelper(WeakReference(this))
        locationPermissionHelper.checkPermissions {
            onMapReady()
        }

        btnStartTrack = findViewById(R.id.btnStartTrack)
        btnStartTrack.setOnClickListener(View.OnClickListener() {
            getLocationUpdate();
            startLocationUpdates();
            Toast.makeText(this,"Iniiciado",Toast.LENGTH_SHORT)

        })
        btnStopTrack = findViewById(R.id.btnStopTrack)
        btnStopTrack.setOnClickListener(View.OnClickListener {
            stopLocationUpdates();
            Toast.makeText(this,"Apagado",Toast.LENGTH_SHORT)
        })



    }

    private fun onMapReady() {
        mapView.getMapboxMap().setCamera(
            CameraOptions.Builder()
                .zoom(14.0)
                .build()
        )
        mapView.getMapboxMap().loadStyleUri(
            Style.MAPBOX_STREETS
        ) {
            initLocationComponent()
            setupGesturesListener()
        }
    }
    private fun startLocationUpdates (){

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
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

        fusedLocationProviderClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            Looper.getMainLooper()
        )
        //startForegroundService(125,builder.build)

    }
    private fun stopLocationUpdates() {
        //tvCurrentCordinates.text="Se ha detenido"
        val removeTask = fusedLocationProviderClient.removeLocationUpdates(locationCallback)
        removeTask.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Log.d(ContentValues.TAG, "Location Callback removed.")

            } else {
                Log.d(ContentValues.TAG, "Failed to remove Location Callback.")
            }
        }
    }
    private fun setupGesturesListener() {
        mapView.gestures.addOnMoveListener(onMoveListener)
    }
 /*   private fun testComponente (){
        Plugin.Mapbox.LocationManager.requestLocationUpdates(object : LocationListener {
            override fun onLocationChanged(location: Location) {
                // Guardar los datos del GPS en algún lugar
            }

            override fun onStatusChanged(provider: String, status: Int, extras: Bundle?) {
                // Hacer algo si cambia el estado del proveedor de GPS
            }

            override fun onProviderEnabled(provider: String) {
                // Hacer algo si se activa el proveedor de GPS
            }

            override fun onProviderDisabled(provider: String) {
                // Hacer algo si se desactiva el proveedor de GPS
            }
        }, 10000)
    }*/
 private fun convertPointToLocation(point: Point): Location {
     val newLocation = Location("Mapbox")
     newLocation.latitude = point.latitude()
     newLocation.longitude = point.longitude()
     return newLocation
 }
    private fun initLocationComponent() {
        //Funcion se ejecuta cuando el mapa esta listo
        //Manejador de eventos cuando la ubicacion cambia
        val locationComponentPlugin = mapView.location
        locationComponentPlugin.updateSettings {
            this.enabled = true
            this.locationPuck = LocationPuck2D(
                topImage = AppCompatResources.getDrawable(
                    this@MainActivity,
                    com.mapbox.maps.plugin.locationcomponent.R.drawable.mapbox_user_icon
                ),
                bearingImage = AppCompatResources.getDrawable(
                    this@MainActivity,
                    com.mapbox.maps.plugin.locationcomponent.R.drawable.mapbox_user_bearing_icon
                ),
                shadowImage = AppCompatResources.getDrawable(
                    this@MainActivity,
                    com.mapbox.maps.plugin.locationcomponent.R.drawable.mapbox_user_stroke_icon
                ),
                scaleExpression = interpolate {
                    linear()
                    zoom()
                    stop {
                        literal(0.0)
                        literal(0.6)
                    }
                    stop {
                        literal(20.0)
                        literal(1.0)
                    }
                }.toJson()
            )

            //
        }
        locationComponentPlugin.addOnIndicatorPositionChangedListener(
            onIndicatorPositionChangedListener
        )
        locationComponentPlugin.addOnIndicatorBearingChangedListener(
            onIndicatorBearingChangedListener
        )
     locationComponentPlugin.addOnIndicatorPositionChangedListener { newPointLocation ->
         var newLocation = convertPointToLocation(newPointLocation)
         routeLocations.add(convertPointToLocation(newPointLocation))

         //lastLocation = convertPointToLocation(newPointLocation);

         // Puedes realizar otras acciones aquí, como actualizar la interfaz



     }

    }

    private fun onCameraTrackingDismissed() {
        // Disapara mensaje cuando deja de seguir el camera track
        Toast.makeText(this, "onCameraTrackingDismissed", Toast.LENGTH_SHORT).show()
        mapView.location
            .removeOnIndicatorPositionChangedListener(onIndicatorPositionChangedListener)
        mapView.location
            .removeOnIndicatorBearingChangedListener(onIndicatorBearingChangedListener)
        mapView.gestures.removeOnMoveListener(onMoveListener)
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView.location
            .removeOnIndicatorBearingChangedListener(onIndicatorBearingChangedListener)
        mapView.location
            .removeOnIndicatorPositionChangedListener(onIndicatorPositionChangedListener)
        mapView.gestures.removeOnMoveListener(onMoveListener)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        locationPermissionHelper.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
}