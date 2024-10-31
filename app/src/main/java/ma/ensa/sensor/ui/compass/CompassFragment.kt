package ma.ensa.sensor.ui.compass

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.RotateAnimation
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment

import ma.ensa.sensor.R

class CompassFragment : Fragment(), SensorEventListener {


    private lateinit var image: ImageView

    private var currentDegree = 0f

    private lateinit var sensorManager: SensorManager
    private var compassSensor: Sensor? = null

    private lateinit var tvHeading: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val root = inflater.inflate(R.layout.fragment_compass, container, false)
        image = root.findViewById(R.id.imageViewCompass)
        tvHeading = root.findViewById(R.id.tvHeading)
        return root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sensorManager = requireActivity().getSystemService(Context.SENSOR_SERVICE) as SensorManager
        compassSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION)

        if (compassSensor == null) {
            Toast.makeText(context, R.string.message_neg, Toast.LENGTH_LONG).show()
        }
    }

    override fun onResume() {
        super.onResume()
        compassSensor?.let {
            sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_UI)
        }
    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(this)
    }

    override fun onSensorChanged(event: SensorEvent) {

        val degree = Math.round(event.values[0])
        tvHeading.text = "Heading: ${degree.toFloat()} degrees"

        val ra = RotateAnimation(
            currentDegree,
            -degree.toFloat(),
            Animation.RELATIVE_TO_SELF, 0.5f,
            Animation.RELATIVE_TO_SELF,
            0.5f
        )


        ra.duration = 210
        ra.fillAfter = true

        image.startAnimation(ra)
        currentDegree = -degree.toFloat()
    }

    override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {
    }
}
