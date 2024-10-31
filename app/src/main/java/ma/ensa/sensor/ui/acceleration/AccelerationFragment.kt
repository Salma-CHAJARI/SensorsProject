package ma.ensa.sensor.ui.acceleration

import android.content.Context
import android.graphics.Color
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
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

class AccelerationFragment : Fragment(), SensorEventListener {

    private lateinit var mSensorManager: SensorManager
    private lateinit var mAccelerometer: Sensor
    private lateinit var xValue: TextView
    private lateinit var yValue: TextView
    private lateinit var zValue: TextView
    private lateinit var statusTextView: TextView
    private lateinit var graphX: GraphView
    private lateinit var graphY: GraphView
    private lateinit var graphZ: GraphView
    private val entriesX = mutableListOf<DataPoint>()
    private val entriesY = mutableListOf<DataPoint>()
    private val entriesZ = mutableListOf<DataPoint>()

    private var index = 0
    private val handler = Handler(Looper.getMainLooper())

    override fun onCreate(@Nullable savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mSensorManager = requireActivity().getSystemService(Context.SENSOR_SERVICE) as SensorManager
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) ?: run {
            Toast.makeText(context, "Accelerometer not available", Toast.LENGTH_LONG).show()
            return
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val root = inflater.inflate(R.layout.fragment_acceleration, container, false)
        xValue = root.findViewById(R.id.xValue)
        yValue = root.findViewById(R.id.yValue)
        zValue = root.findViewById(R.id.zValue)
        statusTextView = root.findViewById(R.id.statusTextView)
        graphX = root.findViewById(R.id.graphX)
        graphY = root.findViewById(R.id.graphY)
        graphZ = root.findViewById(R.id.graphZ)

        startSimulation()
        return root
    }

    private fun startSimulation() {
        handler.post(runnable)
    }

    private val runnable = object : Runnable {
        override fun run() {
            val simulatedAcceleration = 20 + Random.nextFloat() * 10

            handler.postDelayed(this, 2000)
        }
    }

    override fun onResume() {
        super.onResume()
        mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL)
        resetGraphs()
    }

    override fun onPause() {
        super.onPause()
        mSensorManager.unregisterListener(this)
        handler.removeCallbacks(runnable)
    }

    override fun onSensorChanged(event: SensorEvent) {
        if (event.sensor.type == Sensor.TYPE_ACCELEROMETER) {
            val x = event.values[0]
            val y = event.values[1]
            val z = event.values[2]

            xValue.text = "X: $x"
            yValue.text = "Y: $y"
            zValue.text = "Z: $z"

            addEntry(x, 'X')
            addEntry(y, 'Y')
            addEntry(z, 'Z')

            val userState = determineUserState(x, y, z)
            statusTextView.text = userState
        }
    }

    private fun addEntry(gravityRotation: Float, axis: Char) {
        val series = LineGraphSeries<DataPoint>()
        series.color = when (axis) {
            'X' -> Color.RED
            'Y' -> Color.GREEN
            'Z' -> Color.BLUE
            else -> Color.BLACK
        }

        when (axis) {
            'X' -> {
                entriesX.add(DataPoint(index.toDouble(), gravityRotation.toDouble()))
                if (entriesX.size > 100) entriesX.removeAt(0)
                series.resetData(entriesX.toTypedArray())
                graphX.removeAllSeries()
                graphX.addSeries(series)
            }
            'Y' -> {
                entriesY.add(DataPoint(index.toDouble(), gravityRotation.toDouble()))
                if (entriesY.size > 100) entriesY.removeAt(0)
                series.resetData(entriesY.toTypedArray())
                graphY.removeAllSeries()
                graphY.addSeries(series)
            }
            'Z' -> {
                entriesZ.add(DataPoint(index.toDouble(), gravityRotation.toDouble()))
                if (entriesZ.size > 100) entriesZ.removeAt(0)
                series.resetData(entriesZ.toTypedArray())
                graphZ.removeAllSeries()
                graphZ.addSeries(series)
            }
        }
        index++
        Log.d("size", "X: ${entriesX.size}, Y: ${entriesY.size}, Z: ${entriesZ.size}")
    }

    private fun determineUserState(x: Float, y: Float, z: Float): String {
        return when {
            z > 9 && Math.abs(x) < 1 && Math.abs(y) < 1 -> "Standing"
            z < 9 && Math.abs(y) > 5 -> "Jump"
            Math.abs(y) < 1 && Math.abs(x) < 1 -> "Sitting"
            else -> "Walking"
        }
    }

    override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {
    }

    private fun resetGraphs() {
        entriesX.clear()
        entriesY.clear()
        entriesZ.clear()
        index = 0
        graphX.removeAllSeries()
        graphY.removeAllSeries()
        graphZ.removeAllSeries()
    }
}
