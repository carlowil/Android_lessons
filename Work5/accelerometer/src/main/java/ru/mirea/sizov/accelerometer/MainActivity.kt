package ru.mirea.sizov.accelerometer

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat


class MainActivity : AppCompatActivity(), SensorEventListener {

    private lateinit var  azimuthTextView: TextView
    private lateinit var pitchTextView: TextView
    private lateinit var rollTextView: TextView
    private lateinit var sensorManager: SensorManager
    private lateinit var accelerometerSensor: Sensor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        accelerometerSensor = sensorManager
            .getDefaultSensor(Sensor.TYPE_ACCELEROMETER)!!
        azimuthTextView = findViewById(R.id.textViewAzimuth)
        pitchTextView = findViewById(R.id.textViewPitch)
        rollTextView = findViewById(R.id.textViewRoll)
    }
    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(this)
    }

    override fun onResume() {
        super.onResume()
        sensorManager.registerListener(
            this, accelerometerSensor,
            SensorManager.SENSOR_DELAY_NORMAL
        )
    }

    override fun onSensorChanged(event: SensorEvent) {
        if (event.sensor.type == Sensor.TYPE_ACCELEROMETER) {
            val valueAzimuth = event.values[0]
            val valuePitch = event.values[1]
            val valueRoll = event.values[2]
            azimuthTextView.text = "Azimuth: $valueAzimuth"
            pitchTextView.text = "Pitch: $valuePitch"
            rollTextView.text = "Roll: $valueRoll"
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
}