package ma.ensa.sensor.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class HomeViewModel : ViewModel() {

    private val _availableSensors = MutableLiveData<List<String>>().apply {
        value = listOf("Temperature: 20°C", "Humidity: 60%", "Pressure: 1013 hPa")
    }
    val availableSensors: LiveData<List<String>> = _availableSensors

    private val _alerts = MutableLiveData<List<String>>().apply {
        value = listOf("Alert: High Temperature")
    }
    val alertsData: LiveData<List<String>> = _alerts


    private val _news = MutableLiveData<String>().apply {
        value = "Update: New sensor added"
    }
    val newsData: LiveData<String> = _news

    private val _chartData = MutableLiveData<List<Float>>().apply {
        value = listOf(1.0f, 2.0f, 3.0f, 4.0f, 5.0f)
    }
    val chartData: LiveData<List<Float>> = _chartData

    private val _text = MutableLiveData<String>().apply {
        value = "Welcome to the homepage"
    }
    val text: LiveData<String> = _text
}
