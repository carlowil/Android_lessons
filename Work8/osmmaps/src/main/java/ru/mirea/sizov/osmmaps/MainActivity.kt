package ru.mirea.sizov.osmmaps

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.preference.PreferenceManager
import org.osmdroid.config.Configuration
import org.osmdroid.library.R
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.ScaleBarOverlay
import org.osmdroid.views.overlay.compass.CompassOverlay
import org.osmdroid.views.overlay.compass.InternalCompassOrientationProvider
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay
import ru.mirea.sizov.osmmaps.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var mapView: MapView
    private var isWork = false
    private val REQUEST_CODE_PERMISSION = 100

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        Configuration.getInstance().load(getApplicationContext(),
            PreferenceManager.getDefaultSharedPreferences(getApplicationContext()));

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.main)
        mapView = binding.mapView

        mapView.setZoomRounding(true)
        mapView.setMultiTouchControls(true)

        val mapController = mapView.controller
        mapController.setZoom(15.0)
        val startPoint = GeoPoint(55.794229, 37.700772)
        mapController.setCenter(startPoint)

        val dm = this.resources.displayMetrics
        val scaleBarOverlay = ScaleBarOverlay(mapView)
        scaleBarOverlay.setCentred(true)
        scaleBarOverlay.setScaleBarOffset(dm.widthPixels / 2, 10)
        mapView.overlays.add(scaleBarOverlay)

        val compassOverlay = CompassOverlay(
            applicationContext, InternalCompassOrientationProvider(
                applicationContext
            ), mapView
        )
        compassOverlay.enableCompass()
        mapView.overlays.add(compassOverlay)


        checkPermissions()

        if (isWork) {
            val locationMeOverlay = MyLocationNewOverlay(
                GpsMyLocationProvider(applicationContext), mapView
            )
            locationMeOverlay.enableMyLocation()
            mapView.overlays.add(locationMeOverlay)

            addNewPoint(GeoPoint(55.77818042749897, 37.89894526621366), "Дом", "Мой уютный домик")
            addNewPoint(GeoPoint(55.79353717399762, 37.7013616459474), "РТУ МИРЭА", "Вузик")
            addNewPoint(GeoPoint(55.75156490609827, 37.61724045975332), "Кремль", "Зато красный")
            addNewPoint(GeoPoint(55.73177828967126, 37.60376303446609), "Парк Горького", "Сколько раз ты здесь был?")
            addNewPoint(GeoPoint(55.73658094344443, 37.591244404256415), "Бургер Кинг", "Бургер кинг... А дальше вы сами знаете)")

        } else {
            Toast.makeText(this, "Требуются разрешения", Toast.LENGTH_SHORT).show()
        }

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    public override fun onResume() {
        super.onResume()
        Configuration.getInstance().load(
            applicationContext,
            PreferenceManager.getDefaultSharedPreferences(applicationContext)
        )
        mapView.onResume()
    }

    public override fun onPause() {
        super.onPause()
        Configuration.getInstance().save(
            applicationContext,
            PreferenceManager.getDefaultSharedPreferences(applicationContext)
        )
        mapView.onPause()
    }

    private fun addNewPoint(point: GeoPoint, title: String, description: String) {
        val marker = Marker(mapView)
        marker.position = point
        marker.setOnMarkerClickListener { marker, mapView ->
            Toast.makeText(applicationContext, description, Toast.LENGTH_SHORT).show()
            false
        }
        mapView.overlays.add(marker)
        marker.icon = ResourcesCompat.getDrawable(
            resources,
            R.drawable.osm_ic_follow_me_on, null
        )
        marker.title = title
    }

    private fun checkPermissions() {
        val fineLocationPermissionStatus = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
        if (fineLocationPermissionStatus == PackageManager.PERMISSION_GRANTED) {
            isWork = true
        } else {
            ActivityCompat.requestPermissions(
                this, arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION
                ), REQUEST_CODE_PERMISSION
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSION) {
            //	permission	granted
            isWork = (grantResults.size > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED)
        }
    }
}