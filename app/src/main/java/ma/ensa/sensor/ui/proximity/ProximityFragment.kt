package ma.ensa.sensor.ui.proximity

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
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.jjoe64.graphview.GraphView
import com.jjoe64.graphview.series.DataPoint
import com.jjoe64.graphview.series.LineGraphSeries
import ma.ensa.sensor.R
import kotlin.random.Random

class ProximityFragment : Fragment(), SensorEventListener {
    private lateinit var graph: GraphView
    private lateinit var sensorManager: SensorManager
    private var proximitySensor: Sensor? = null
    private var series: LineGraphSeries<DataPoint>? = null
    private val entries = ArrayList<DataPoint>()
    private var lastXValue = 0.0
    private val handler = Handler()
    private var runnable: Runnable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sensorManager = requireActivity().getSystemService(Context.SENSOR_SERVICE) as SensorManager
        proximitySensor = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY)

        if (proximitySensor == null) {
            Toast.makeText(context, R.string.message_neg, Toast.LENGTH_LONG).show()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val root = inflater.inflate(R.layout.fragment_proximity, container, false)
        graph = root.findViewById(R.id.graph)

        series = LineGraphSeries<DataPoint>().apply {
            color = Color.RED
        }
        graph.addSeries(series)

        startSimulation()
        return root
    }

    private fun startSimulation() {
        runnable = object : Runnable {
            override fun run() {

                val simulatedProximity = Random.nextFloat() * 5
                addEntry(simulatedProximity)
                handler.postDelayed(this, 2000)
            }
        }
        handler.post(runnable!!)
    }

    private fun addEntry(proximity: Float) {
        val dataPoint = DataPoint(lastXValue, proximity.toDouble())
        entries.add(dataPoint)
        series?.resetData(entries.toTypedArray())
        lastXValue++
        Log.d("ProximityFragment", "Proximity Value: $proximity")
    }

    override fun onResume() {
        super.onResume()
        proximitySensor?.let {
            sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_NORMAL)
        }
    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(this)
        handler.removeCallbacks(runnable!!)
    }

    override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {

    }

    override fun onSensorChanged(event: SensorEvent) {
        if (event.sensor.type == Sensor.TYPE_PROXIMITY) {
            val proximityValue = event.values[0]
            Log.d("ProximityFragment", "Proximity Value: $proximityValue")

            addEntry(proximityValue)
        }
    }
}
