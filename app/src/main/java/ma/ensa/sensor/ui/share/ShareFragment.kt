package ma.ensa.sensor.ui.share

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import androidx.fragment.app.Fragment
import ma.ensa.sensor.R

class ShareFragment : Fragment() {

    private val temperatureValue = "25°C"
    private val humidityValue = "60%"
    private val proximityValue = "10 cm"
    private val magneticValue = "North"
    private val accelerationValue = "1.0 m/s²"
    private val compassValue = "N"
    private val rotationValue = "0°"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_share, container, false)

        val checkboxTemperature: CheckBox = root.findViewById(R.id.checkbox_temperature)
        val checkboxHumidity: CheckBox = root.findViewById(R.id.checkbox_humidity)
        val checkboxProximity: CheckBox = root.findViewById(R.id.checkbox_proximity)
        val checkboxMagnetic: CheckBox = root.findViewById(R.id.checkbox_magnetic)
        val checkboxAcceleration: CheckBox = root.findViewById(R.id.checkbox_acceleration)
        val checkboxCompass: CheckBox = root.findViewById(R.id.checkbox_compass)
        val checkboxRotation: CheckBox = root.findViewById(R.id.checkbox_rotation)


        val btnShare: Button = root.findViewById(R.id.btn_share)


        btnShare.setOnClickListener {
            val selectedSensors = StringBuilder("")

            if (checkboxTemperature.isChecked) selectedSensors.append("Temperature: $temperatureValue\n")
            if (checkboxHumidity.isChecked) selectedSensors.append("Humidity: $humidityValue\n")
            if (checkboxProximity.isChecked) selectedSensors.append("Proximity: $proximityValue\n")
            if (checkboxMagnetic.isChecked) selectedSensors.append("Magnetic: $magneticValue\n")
            if (checkboxAcceleration.isChecked) selectedSensors.append("Acceleration: $accelerationValue\n")
            if (checkboxCompass.isChecked) selectedSensors.append("Compass: $compassValue\n")
            if (checkboxRotation.isChecked) selectedSensors.append("Rotation: $rotationValue\n")

            val intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, selectedSensors.toString())
                type = "text/plain"
            }
            startActivity(Intent.createChooser(intent, "Share with"))
        }

        return root
    }
}
