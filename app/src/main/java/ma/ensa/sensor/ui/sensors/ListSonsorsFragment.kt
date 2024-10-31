package ma.ensa.sensor.ui.sensors

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ma.ensa.sensor.R
import ma.ensa.sensor.beans.SensorItem

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
class ListSonsorsFragment : Fragment() {

    private var mColumnCount = 1
    private var mListener: OnListFragmentInteractionListener? = null

    companion object {
        private const val ARG_COLUMN_COUNT = "column-count"

        @JvmStatic
        fun newInstance(columnCount: Int) = ListSonsorsFragment().apply {
            arguments = Bundle().apply {
                putInt(ARG_COLUMN_COUNT, columnCount)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            mColumnCount = it.getInt(ARG_COLUMN_COUNT)
        }
    }

    private fun loadSensor(): List<SensorItem> {
        val sensors = mutableListOf<SensorItem>()
        val sensorManager = activity?.getSystemService(Context.SENSOR_SERVICE) as SensorManager
        val sListe: List<Sensor> = sensorManager.getSensorList(Sensor.TYPE_ALL)

        sListe.forEachIndexed { index, sensor ->
            val resolution = sensor.resolution.toString()
            val power = sensor.power.toString()
            val maxRange = sensor.maximumRange.toString()
            val intType = sensor.type.toString()

            val maxDataRate = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                val maxDelay = sensor.maxDelay
                if (maxDelay > 0) {
                    String.format("%.2f Hz", 1.0 / (maxDelay / 1_000_000.0))
                } else {
                    "N/A"
                }
            } else {
                "N/A"
            }

            sensors.add(SensorItem(
                id = (index + 1).toString(),
                name = sensor.name,
                type = sensorTypeToString(sensor.type),
                vendor = sensor.vendor,
                version = sensor.version.toString(),
                resolution = resolution,
                power = power,
                maxRange = maxRange,
                intType = intType,
                maxDataRate = maxDataRate
            ))
        }
        return sensors
    }

    private fun sensorTypeToString(sensorType: Int): String {
        return when (sensorType) {
            Sensor.TYPE_ACCELEROMETER -> "Accelerometer"
            Sensor.TYPE_AMBIENT_TEMPERATURE, Sensor.TYPE_TEMPERATURE -> "Ambient Temperature"
            Sensor.TYPE_GAME_ROTATION_VECTOR -> "Game Rotation Vector"
            Sensor.TYPE_GEOMAGNETIC_ROTATION_VECTOR -> "Geomagnetic Rotation Vector"
            Sensor.TYPE_GRAVITY -> "Gravity"
            Sensor.TYPE_GYROSCOPE -> "Gyroscope"
            Sensor.TYPE_GYROSCOPE_UNCALIBRATED -> "Gyroscope Uncalibrated"
            Sensor.TYPE_HEART_RATE -> "Heart Rate Monitor"
            Sensor.TYPE_LIGHT -> "Light"
            Sensor.TYPE_LINEAR_ACCELERATION -> "Linear Acceleration"
            Sensor.TYPE_MAGNETIC_FIELD -> "Magnetic Field"
            Sensor.TYPE_MAGNETIC_FIELD_UNCALIBRATED -> "Magnetic Field Uncalibrated"
            Sensor.TYPE_ORIENTATION -> "Orientation"
            Sensor.TYPE_PRESSURE -> "Pressure"
            Sensor.TYPE_PROXIMITY -> "Proximity"
            Sensor.TYPE_RELATIVE_HUMIDITY -> "Relative Humidity"
            Sensor.TYPE_ROTATION_VECTOR -> "Rotation Vector"
            Sensor.TYPE_SIGNIFICANT_MOTION -> "Significant Motion"
            Sensor.TYPE_STEP_COUNTER -> "Step Counter"
            Sensor.TYPE_STEP_DETECTOR -> "Step Detector"
            else -> "Unknown"
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_listsonsors_list, container, false)
        if (view is RecyclerView) {
            context?.let { context ->
                view.layoutManager = if (mColumnCount <= 1) {
                    LinearLayoutManager(context)
                } else {
                    GridLayoutManager(context, mColumnCount)
                }
                view.adapter = ListSonsorsFragmentRecyclerViewAdapter(loadSensor(), mListener)
            }
        }
        return view
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnListFragmentInteractionListener) {
            mListener = context
        }
    }

    override fun onDetach() {
        super.onDetach()
        mListener = null
    }

    interface OnListFragmentInteractionListener {
        fun onListFragmentInteraction(item: SensorItem)
    }
}









