package ru.mirea.sizov.yandexdriver

import android.Manifest
import ru.mirea.sizov.yandexdriver.R
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.PointF
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.RequestPoint
import com.yandex.mapkit.RequestPointType
import com.yandex.mapkit.directions.DirectionsFactory
import com.yandex.mapkit.directions.driving.DrivingOptions
import com.yandex.mapkit.directions.driving.DrivingRoute
import com.yandex.mapkit.directions.driving.DrivingRouter
import com.yandex.mapkit.directions.driving.DrivingSession
import com.yandex.mapkit.directions.driving.VehicleOptions
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.layers.ObjectEvent
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.map.IconStyle
import com.yandex.mapkit.map.MapObjectCollection
import com.yandex.mapkit.map.RotationType
import com.yandex.mapkit.mapview.MapView
import com.yandex.mapkit.user_location.UserLocationLayer
import com.yandex.mapkit.user_location.UserLocationObjectListener
import com.yandex.mapkit.user_location.UserLocationView
import com.yandex.runtime.Error
import com.yandex.runtime.image.ImageProvider
import com.yandex.runtime.network.NetworkError
import com.yandex.runtime.network.RemoteError
import ru.mirea.sizov.yandexdriver.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity(), DrivingSession.DrivingRouteListener,
    UserLocationObjectListener {

    private lateinit var binding: ActivityMainBinding
    private lateinit var mapView: MapView
    private var isWork = false
    private val REQUEST_CODE_PERMISSION = 100
    private val ROUTE_START_LOCATION: Point = Point(55.77939742314377, 37.896998454809385)
    private val ROUTE_END_LOCATION: Point = Point(55.79355463972578, 37.701451345568344)
    private val SCREEN_CENTER: Point = Point(
        (ROUTE_START_LOCATION.getLatitude() + ROUTE_END_LOCATION.getLatitude()) / 2,
        (ROUTE_START_LOCATION.getLongitude() + ROUTE_END_LOCATION.getLongitude()) / 2
    )
    private lateinit var userLocationLayer : UserLocationLayer
    private lateinit var drivingRouter:DrivingRouter
    private lateinit var drivingSession: DrivingSession
    private lateinit var mapObjects:MapObjectCollection
    private val colors = intArrayOf(-0x10000, -0xff0100, 0x00FFBBBB, -0xffff01)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        MapKitFactory.initialize(this)
        DirectionsFactory.initialize(this)
        mapView = binding.mapview
        checkPermissions()
        if (isWork) {
            mapView.map.isRotateGesturesEnabled = false
            mapView.map.move(CameraPosition(SCREEN_CENTER, 10f, 0f, 0f))

            drivingRouter = DirectionsFactory.getInstance().createDrivingRouter()
            mapObjects = mapView.map.mapObjects.addCollection()

            val userLoc = ROUTE_START_LOCATION
            submitRequest(userLoc)

            val marker = mapView.map.mapObjects.addPlacemark(
                ROUTE_END_LOCATION,
                ImageProvider.fromResource(this, R.drawable.location)
            )
            val iconStyle = IconStyle()
            iconStyle.setScale(0.3f)
            marker.setIconStyle(iconStyle)
            marker.addTapListener { mapObject, point ->
                Toast.makeText(
                    this@MainActivity,
                    "Лучший РТУ МИРЭА",
                    Toast.LENGTH_SHORT
                ).show()
                false
            }
        } else {
            Toast.makeText(this, "Требуются разрешения", Toast.LENGTH_SHORT).show()
        }

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
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

    override fun onStart() {
        super.onStart()
        mapView.onStart()
        MapKitFactory.getInstance().onStart()
    }

    override fun onStop() {
        super.onStop()
        mapView.onStop()
        MapKitFactory.getInstance().onStop()
    }

    private fun submitRequest(start: Point) {
        val drivingOptions = DrivingOptions()
        val vehicleOptions = VehicleOptions()

        drivingOptions.setRoutesCount(4)

        val requestPoints = ArrayList<RequestPoint>()
        requestPoints.add(
            RequestPoint(
                start,
                RequestPointType.WAYPOINT,
                null
            )
        )
        requestPoints.add(
            RequestPoint(
                ROUTE_END_LOCATION,
                RequestPointType.WAYPOINT,
                null
            )
        )

        drivingSession = drivingRouter.requestRoutes(
            requestPoints, drivingOptions,
            vehicleOptions, this
        )
    }

    override fun onDrivingRoutes(drivingList: MutableList<DrivingRoute>) {
        drivingList.forEachIndexed { index, drivingRoute ->
            val color = colors[index]
            mapObjects.addPolyline(drivingRoute.geometry).setStrokeColor(color)
        }
    }

    override fun onDrivingRoutesError(error: Error) {
        var errorMessage = "UnknownError"
        if (error is RemoteError) {
            errorMessage = "RemoteError"
        } else if (error is NetworkError) {
            errorMessage = "NetworkError"
        }
        Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
    }

    override fun onObjectRemoved(p0: UserLocationView) {

    }

    override fun onObjectUpdated(p0: UserLocationView, p1: ObjectEvent) {

    }

    override fun onObjectAdded(userLocationView: UserLocationView) {
        userLocationLayer.setAnchor(
            PointF(
                (mapView.width * 0.5).toFloat(),
                (mapView.height * 0.5).toFloat()
            ),
            PointF(
                (mapView.width * 0.5).toFloat(),
                (mapView.height * 0.83).toFloat()
            )
        )

        userLocationView.arrow.setIcon(
            ImageProvider.fromResource(
                this,
                com.yandex.maps.mobile.R.drawable.arrow
            )
        )

        val pinIcon = userLocationView.pin.useCompositeIcon()
        pinIcon.setIcon(
            "pin",
            ImageProvider.fromResource(this, R.drawable.ic_launcher_foreground),
            IconStyle().setAnchor(PointF(0.5f, 0.5f))
                .setRotationType(RotationType.ROTATE)
                .setZIndex(1f)
                .setScale(0.5f)
        )

        userLocationView.accuracyCircle.fillColor = Color.BLUE and -0x66000001
    }

}