package ma.ensa.sensor.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.jjoe64.graphview.GraphView
import com.jjoe64.graphview.series.DataPoint
import com.jjoe64.graphview.series.LineGraphSeries
import ma.ensa.sensor.R

class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val root = inflater.inflate(R.layout.fragment_home, container, false)
        val sensorDataTextView: TextView = root.findViewById(R.id.text_sensor_values)
        val alertsTextView: TextView = root.findViewById(R.id.text_alert_values)
        val newsTextView: TextView = root.findViewById(R.id.text_news)
        val graph: GraphView = root.findViewById(R.id.graph_view)

        homeViewModel.availableSensors.observe(viewLifecycleOwner) { sensors ->
            sensorDataTextView.text = sensors.joinToString("\n")
            Log.d("HomeFragment", "Available Sensors: $sensors")
        }

        homeViewModel.alertsData.observe(viewLifecycleOwner) { alerts ->
            alertsTextView.text = alerts.joinToString("\n")
        }

        homeViewModel.newsData.observe(viewLifecycleOwner) { news ->
            newsTextView.text = news
        }

        homeViewModel.chartData.observe(viewLifecycleOwner) { chartData ->
            setupGraph(graph, chartData)
        }

        val controlButton: Button = root.findViewById(R.id.btn_control)
        controlButton.setOnClickListener {

            findNavController().navigate(R.id.nav_list)
        }

        return root
    }

    private fun setupGraph(graph: GraphView, data: List<Float>) {
        val series = LineGraphSeries<DataPoint>()
        data.forEachIndexed { index, value ->
            series.appendData(DataPoint(index.toDouble(), value.toDouble()), true, data.size)
        }
        graph.addSeries(series)
        graph.title = "Sensor Data"
        graph.gridLabelRenderer.verticalAxisTitle = "Values"
        graph.gridLabelRenderer.horizontalAxisTitle = "Time"
    }
}
