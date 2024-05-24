package ru.mirea.sizov.mireaproject

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import androidx.preference.PreferenceManager
import org.osmdroid.config.Configuration
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.ScaleBarOverlay
import org.osmdroid.views.overlay.compass.CompassOverlay
import org.osmdroid.views.overlay.compass.InternalCompassOrientationProvider
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [PlacesFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class PlacesFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null
    lateinit var mapView: MapView

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
        return inflater.inflate(R.layout.fragment_places, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mapView = view.findViewById(R.id.mapView)
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
            requireActivity().applicationContext, InternalCompassOrientationProvider(
                requireActivity().applicationContext
            ), mapView
        )
        compassOverlay.enableCompass()
        mapView.overlays.add(compassOverlay)

        val locationMeOverlay = MyLocationNewOverlay(
            GpsMyLocationProvider(requireActivity().applicationContext), mapView
        )
        locationMeOverlay.enableMyLocation()
        mapView.overlays.add(locationMeOverlay)


        addNewPoint(GeoPoint(55.77818042749897, 37.89894526621366), "Новая Третьяковка", "Расположенный на берегу реки музей в здании в стиле модерн с коллекцией произведений русского искусства XX века.")
        addNewPoint(GeoPoint(55.72677089370938, 37.630718726291626), "Московский государственный музей С.А. Есенина", "Дом, в котором в 1911–1918 гг. был прописан поэт С. Есенин, с выставками, посвященными его жизни и творчеству.")
        addNewPoint(GeoPoint(55.75156490609827, 37.61724045975332), "Кремль", "Крепость с комплексом церквей, дворцов и музеев с произведениями искусства и государственными регалиями.")
        addNewPoint(GeoPoint(55.7473382850178, 37.60546848396229), "Государственный музей изобразительных искусств имени А.С. Пушкина", "Музей с предметами старины, скульптурой и живописью, а также временными экспозициями. Памятник классицизма.")
        addNewPoint(GeoPoint(55.76588208281968, 37.630215912798185), "Сретенский Мужской Монастырь", "Православный монастырь XIV века с золотыми куполами и знаменитым мужским хором.")
        addNewPoint(GeoPoint(55.76791693105746, 37.6137791881245), "Московский музей современного искусства", "Постоянная выставка предметов искусства XX и XXI вв., расположенная в особняке в неоклассическом стиле.")
        addNewPoint(GeoPoint(55.76439579533504, 37.57686921748023), "Московский зоопарк", "Созданный для защиты и изучения природы зоопарк, где живут более 8000 животных: белые медведи, слоны и другие.")
        addNewPoint(GeoPoint(55.746488538573985, 37.64527789561357), "Музей русской иконы им. Михаила Абрамова", "Художественный музей с большой частной коллекцией произведений византийской, христианской и русской иконописи.")
    }

    override fun onResume() {
        super.onResume()
        Configuration.getInstance().load(
            requireActivity().applicationContext,
            PreferenceManager.getDefaultSharedPreferences(requireActivity().applicationContext)
        )
        mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        Configuration.getInstance().save(
            requireActivity().applicationContext,
            PreferenceManager.getDefaultSharedPreferences(requireActivity().applicationContext)
        )
        mapView.onPause()
    }

    private fun addNewPoint(point: GeoPoint, title: String, description: String) {
        val marker = Marker(mapView)
        marker.position = point
        marker.setOnMarkerClickListener { marker, mapView ->
            val bundle = Bundle()
            bundle.putString("title", title)
            bundle.putString("desc", description)
            val dialog = PlaceDialog()
            dialog.arguments = bundle
            val manager = requireActivity().supportFragmentManager
            val transaction = manager.beginTransaction()
            dialog.show(transaction, "PlaceDialog")
            false
        }
        mapView.overlays.add(marker)
        marker.icon = ResourcesCompat.getDrawable(
            resources,
            org.osmdroid.library.R.drawable.osm_ic_follow_me_on, null
        )
        marker.title = title
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment PlacesFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            PlacesFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}