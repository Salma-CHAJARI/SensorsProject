package ma.ensa.sensor.ui.magnetic

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
import android.widget.Toast
import androidx.annotation.Nullable
import androidx.fragment.app.Fragment
import com.jjoe64.graphview.GraphView
import com.jjoe64.graphview.series.DataPoint
import com.jjoe64.graphview.series.LineGraphSeries
import ma.ensa.sensor.R
import kotlin.random.Random
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.*

class MagneticFragment : Fragment(), SensorEventListener {
    private lateinit var sensorManager: SensorManager
    private var magneticSensor: Sensor? = null
    private lateinit var decimalFormatter: DecimalFormat
    private val entries = mutableListOf<DataPoint>()
    private lateinit var graph: GraphView
    private lateinit var valueTextView: TextView
    private lateinit var series: LineGraphSeries<DataPoint>
    private var entryCount = 0
    private val handler = Handler()
    private var runnable: Runnable? = null

    override fun onCreate(@Nullable savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sensorManager = requireActivity().getSystemService(Context.SENSOR_SERVICE) as SensorManager
        magneticSensor = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)
        if (magneticSensor == null) {
            Toast.makeText(context, R.string.message_neg, Toast.LENGTH_LONG).show()
        }

        val symbols = DecimalFormatSymbols(Locale.US).apply {
            decimalSeparator = '.'
        }
        decimalFormatter = DecimalFormat("#.000", symbols)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val root = inflater.inflate(R.layout.fragment_magnetic, container, false)
        valueTextView = root.findViewById(R.id.value)
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
                val simulatedMagneticValue = 30 + Random.nextFloat() * 70
                addEntry(simulatedMagneticValue.toDouble())
                handler.postDelayed(this, 2000)
            }
        }
        handler.post(runnable!!)
    }

    override fun onResume() {
        super.onResume()
        sensorManager.registerListener(this, magneticSensor, SensorManager.SENSOR_DELAY_NORMAL)
        entries.clear()
        entryCount = 0
    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(this)
        entries.clear()
        handler.removeCallbacks(runnable!!)
    }

    override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {}

    override fun onSensorChanged(event: SensorEvent) {
        if (event.sensor.type == Sensor.TYPE_MAGNETIC_FIELD) {
            val magX = event.values[0]
            val magY = event.values[1]
            val magZ = event.values[2]
            val magnitude = Math.sqrt((magX * magX + magY * magY + magZ * magZ).toDouble())
            valueTextView.text = "${decimalFormatter.format(magnitude)} µTesla"
            addEntry(magnitude)
        }
    }

    private fun addEntry(value: Double) {
        entries.add(DataPoint(entryCount.toDouble(), value))
        series.resetData(entries.toTypedArray())
        entryCount++
        Log.d("MagneticFragment", "Size: ${entries.size}, Last Value: $value")
    }
}
