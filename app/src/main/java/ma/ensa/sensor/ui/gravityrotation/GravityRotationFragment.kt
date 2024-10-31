package ma.ensa.sensor.ui.gravityrotation

import android.content.Context
import android.graphics.Color
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.Nullable
import androidx.fragment.app.Fragment
import com.jjoe64.graphview.series.DataPoint
import com.jjoe64.graphview.series.LineGraphSeries
import com.jjoe64.graphview.GraphView
import ma.ensa.sensor.R
import kotlin.random.Random

class GravityRotationFragment : Fragment(), SensorEventListener {

    private lateinit var sensorManager: SensorManager
    private var gravitySensor: Sensor? = null
    private var rotationSensor: Sensor? = null
    private lateinit var gravityText: TextView
    private lateinit var rotationText: TextView
    private lateinit var graph: GraphView
    private val gravitySeries = LineGraphSeries<DataPoint>()
    private val pitchSeries = LineGraphSeries<DataPoint>()
    private val rollSeries = LineGraphSeries<DataPoint>()
    private val handler = Handler()
    private var index = 0
    private val entries = ArrayList<DataPoint>()

    override fun onCreate(@Nullable savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sensorManager = requireActivity().getSystemService(Context.SENSOR_SERVICE) as SensorManager
        gravitySensor = sensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY)
        rotationSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_gravity_rotation, container, false)
        gravityText = root.findViewById(R.id.gravity_text)
        rotationText = root.findViewById(R.id.rotation_text)
        graph = root.findViewById(R.id.graph)
        gravitySeries.color = Color.RED
        pitchSeries.color = Color.RED
        rollSeries.color = Color.RED
        graph.addSeries(gravitySeries)
        graph.addSeries(pitchSeries)
        graph.addSeries(rollSeries)

        startSimulation()
        return root
    }
    private fun startSimulation() {
        handler.post(runnable)
    }

    private val runnable = object : Runnable {
        override fun run() {
            val simulatedGravityRotations = 20 + Random.nextFloat() * 10
            addEntry(simulatedGravityRotations)
            handler.postDelayed(this, 2000)
        }
    }

    private fun addEntry(gravityRotation: Float) {
        entries.add(DataPoint(index.toDouble(), gravityRotation.toDouble()))
        if (entries.size > 100) entries.removeAt(0)
        graph.addSeries(LineGraphSeries(entries.toTypedArray()))
        index++
        Log.d("size", entries.size.toString())
    }

    override fun onResume() {
        super.onResume()
        gravitySensor?.let {
            sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_NORMAL)
        }
        rotationSensor?.let {
            sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_NORMAL)
        }
    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(this)
        handler.removeCallbacks(runnable)
    }

    override fun onSensorChanged(event: SensorEvent) {
        when (event.sensor.type) {
            Sensor.TYPE_GRAVITY -> {
                gravityText.text = "Gravity: ${event.values[0]} m/s²"
                addGravityDataPoint(event.values[0].toDouble())
            }
            Sensor.TYPE_ROTATION_VECTOR -> {
                if (event.values.size >= 4) {
                    val (q0, q1, q2, q3) = event.values
                    val pitch = Math.atan2((2 * (q0 * q1 + q2 * q3)).toDouble(),
                        (1 - 2 * (q1 * q1 + q2 * q2)).toDouble())
                    val roll = Math.asin((2 * (q0 * q2 - q3 * q1)).toDouble())

                    rotationText.text = "Pitch: ${Math.toDegrees(pitch).toInt()}°, Roll: ${Math.toDegrees(roll).toInt()}°"
                    addPitchDataPoint(Math.toDegrees(pitch).toDouble())
                    addRollDataPoint(Math.toDegrees(roll).toDouble())
                } else {
                    rotationText.text = "Rotation: Not enough data"
                }
            }
        }
    }

    private fun addGravityDataPoint(gravity: Double) {
        gravitySeries.appendData(DataPoint(index.toDouble(), gravity), true, 100)
    }

    private fun addPitchDataPoint(pitch: Double) {
        pitchSeries.appendData(DataPoint(index.toDouble(), pitch), true, 100)
    }

    private fun addRollDataPoint(roll: Double) {
        rollSeries.appendData(DataPoint(index.toDouble(), roll), true, 100)
    }

    override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {
    }
}
