package ma.ensa.sensor.ui.humidity

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

class HumidityFragment : Fragment(), SensorEventListener {
    private lateinit var graph: GraphView
    private lateinit var series: LineGraphSeries<DataPoint>
    private lateinit var sensorManager: SensorManager
    private var humidSensor: Sensor? = null
    private val handler = Handler()
    private var runnable: Runnable? = null
    private var entryCount = 0
    private val entries = ArrayList<DataPoint>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sensorManager = activity?.getSystemService(Context.SENSOR_SERVICE) as SensorManager
        humidSensor = sensorManager.getDefaultSensor(Sensor.TYPE_RELATIVE_HUMIDITY)

        if (humidSensor == null) {
            Toast.makeText(context, R.string.message_neg, Toast.LENGTH_LONG).show()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val root = inflater.inflate(R.layout.fragment_humidity, container, false)
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
                val simulatedHumidity = 30 + Random.nextFloat() * 70
                addEntry(simulatedHumidity)
                handler.postDelayed(this, 2000)
            }
        }
        handler.post(runnable!!)
    }

    private fun addEntry(humidity: Float) {
        val dataPoint = DataPoint(entryCount.toDouble(), humidity.toDouble())
        entries.add(dataPoint)
        series.resetData(entries.toTypedArray())
        entryCount++
        Log.d("HumidityFragment", "Humidity: $humidity")
    }

    override fun onResume() {
        super.onResume()
        humidSensor?.let {
            sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_NORMAL)
        }
    }

    override fun onPause() {
        super.onPause()
        humidSensor?.let {
            sensorManager.unregisterListener(this, it)
        }
        handler.removeCallbacks(runnable!!)
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}

    override fun onSensorChanged(event: SensorEvent?) {
        event?.let {
            addEntry(it.values[0])
        }
    }
}
