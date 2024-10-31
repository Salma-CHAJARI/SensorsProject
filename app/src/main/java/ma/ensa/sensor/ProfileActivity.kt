package ma.ensa.sensor

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorManager
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class ProfileActivity : AppCompatActivity() {
    private lateinit var sensorManager: SensorManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        val profileImage: ImageView = findViewById(R.id.profile_image)
        val profileName: TextView = findViewById(R.id.profile_name)
        val profileEmail: TextView = findViewById(R.id.profile_email)
        val editProfileButton: Button = findViewById(R.id.edit_profile_button)
        val logoutButton: Button = findViewById(R.id.logout_button)
        val sensorsDetails: TextView = findViewById(R.id.sensors_details)

        val name = "CHAJARI Salma"
        val email = "chajarisalma27@gmail.com"

        profileName.text = name
        profileEmail.text = email

        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        displayAvailableSensors(sensorsDetails)

        editProfileButton.setOnClickListener {
            Toast.makeText(this, "Modifier le Profil", Toast.LENGTH_SHORT).show()
        }

        logoutButton.setOnClickListener {
            Toast.makeText(this, "Déconnexion", Toast.LENGTH_SHORT).show()
        }
    }

    private fun displayAvailableSensors(sensorsDetails: TextView) {
        val sensorList: List<Sensor> = sensorManager.getSensorList(Sensor.TYPE_ALL)

        val sensorDetails = StringBuilder()
        for (sensor in sensorList) {
            sensorDetails.append("Nom: ${sensor.name}\n")
            sensorDetails.append("Type: ${sensor.type}\n")
            sensorDetails.append("Détails: ${sensor.vendor}\n\n")
        }

        sensorsDetails.text = if (sensorDetails.isNotEmpty()) {
            sensorDetails.toString()
        } else {
            "Aucun capteur disponible"
        }
    }
}
