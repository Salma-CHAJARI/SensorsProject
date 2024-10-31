package ma.ensa.sensor.ui.step

import android.Manifest
import android.content.Context
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import ma.ensa.sensor.R

class StepCounterFragment : Fragment(), SensorEventListener {

    private lateinit var stepCountTextView: TextView
    private lateinit var sensorManager: SensorManager
    private var stepCounterSensor: Sensor? = null
    private var stepCount = 0
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var walkAnimation: Animation

    companion object {
        private const val REQUEST_CODE_ACTIVITY_RECOGNITION = 100
        private const val STEP_COUNT_KEY = "step_count"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_step_counter, container, false)
        stepCountTextView = view.findViewById(R.id.step_count)

        walkAnimation = AnimationUtils.loadAnimation(requireContext(), R.anim.walk_animation)
        val imageView = view.findViewById<ImageView>(R.id.imageView2)
        imageView.startAnimation(walkAnimation)

        sharedPreferences = requireActivity().getSharedPreferences("step_count_prefs", Context.MODE_PRIVATE)
        loadStepCount()

        sensorManager = requireActivity().getSystemService(Context.SENSOR_SERVICE) as SensorManager
        stepCounterSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)

        checkPermissions()

        return view
    }

    private fun checkPermissions() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACTIVITY_RECOGNITION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.ACTIVITY_RECOGNITION), REQUEST_CODE_ACTIVITY_RECOGNITION)
        } else {
            startStepCounting()
        }
    }

    private fun startStepCounting() {
        stepCounterSensor?.let {
            sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_UI)
        }
    }

    override fun onResume() {
        super.onResume()
        startStepCounting()
    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(this)
        saveStepCount()

        val imageView = view?.findViewById<ImageView>(R.id.imageView2)
        imageView?.clearAnimation()
    }

    override fun onSensorChanged(event: SensorEvent?) {
        event?.let {
            stepCount = it.values[0].toInt()
            stepCountTextView.text = "Steps: $stepCount"
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
    }

    private fun saveStepCount() {
        sharedPreferences.edit().putInt(STEP_COUNT_KEY, stepCount).apply()
    }

    private fun loadStepCount() {
        stepCount = sharedPreferences.getInt(STEP_COUNT_KEY, 0)
        stepCountTextView.text = "Steps: $stepCount"
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_ACTIVITY_RECOGNITION) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startStepCounting()
            } else {
                stepCountTextView.text = "Permission denied. Steps will not be counted."
            }
        }
    }
}
