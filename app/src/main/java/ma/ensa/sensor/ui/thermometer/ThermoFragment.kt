package ma.ensa.sensor.ui.thermometer

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

class ThermoFragment : Fragment(), SensorEventListener {

    private lateinit var graph: GraphView
    private lateinit var sensorManager: SensorManager
    private var tempSensor: Sensor? = null
    private val entries = ArrayList<DataPoint>()
    private val handler = Handler()
    private lateinit var series: LineGraphSeries<DataPoint>
    private var index = 0
    private lateinit var runnable: Runnable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sensorManager = requireActivity().getSystemService(Context.SENSOR_SERVICE) as SensorManager
        tempSensor = sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE)
        if (tempSensor == null) {
            Toast.makeText(context, R.string.message_neg, Toast.LENGTH_LONG).show()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_thermo, container, false)
        graph = root.findViewById(R.id.chart)

        series = LineGraphSeries()
        series.color = Color.RED
        graph.addSeries(series)

        startSimulation()
        return root
    }

    private fun startSimulation() {
        runnable = Runnable {

            val simulatedTemperature = 20 + Random.nextFloat() * 10
            addEntry(simulatedTemperature)
            handler.postDelayed(runnable, 2000)
        }
        handler.post(runnable)
    }

    private fun addEntry(temperature: Float) {
        entries.add(DataPoint(index.toDouble(), temperature.toDouble()))
        series.resetData(entries.toTypedArray())
        index++

        Log.d("size", entries.size.toString())
    }

    override fun onResume() {
        super.onResume()
        tempSensor?.let {
            sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_NORMAL)
        }
    }

    override fun onPause() {
        super.onPause()
        tempSensor?.let {
            sensorManager.unregisterListener(this)
        }
        handler.removeCallbacks(runnable)
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}

    override fun onSensorChanged(event: SensorEvent) {
        addEntry(event.values[0])
    }
}
