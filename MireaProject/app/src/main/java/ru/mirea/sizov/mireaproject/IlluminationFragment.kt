package ru.mirea.sizov.mireaproject

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [IlluminationFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class IlluminationFragment : Fragment(), SensorEventListener {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var sensorManager : SensorManager
    private lateinit var tempSensor : Sensor
    private lateinit var tempTextView : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sensorManager = activity?.getSystemService(Context.SENSOR_SERVICE) as SensorManager
        tempSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT)!!

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

        return inflater.inflate(R.layout.fragment_illumination, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        tempTextView = requireView().findViewById(R.id.illuminationTextView)
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onResume() {
        super.onResume()
        sensorManager.registerListener(
            this,
            tempSensor,
            SensorManager.SENSOR_DELAY_NORMAL
        )
    }
    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment TemperatureFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            IlluminationFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event!!.sensor.type == Sensor.TYPE_LIGHT) {
            val temp = event.values[0]
            tempTextView.text = "Illumination: $temp lux"
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {

    }
}