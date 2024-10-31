package ma.ensa.sensor.ui.sensors

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

import ma.ensa.sensor.R
import ma.ensa.sensor.beans.SensorItem

class ListSonsorsFragmentRecyclerViewAdapter(
    private val items: List<SensorItem>,
    private val listener: ListSonsorsFragment.OnListFragmentInteractionListener?
) : RecyclerView.Adapter<ListSonsorsFragmentRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_listsonsors, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val sensorItem = items[position]
        holder.bind(sensorItem)

        holder.itemView.setOnClickListener {
            listener?.onListFragmentInteraction(sensorItem)
        }
    }

    override fun getItemCount(): Int = items.size

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val idView: TextView = view.findViewById(R.id.item_number)
        private val nameView: TextView = view.findViewById(R.id.name)
        private val typeView: TextView = view.findViewById(R.id.type)
        private val vendorView: TextView = view.findViewById(R.id.vendor)
        private val versionView: TextView = view.findViewById(R.id.version)
        private val resolutionView: TextView = view.findViewById(R.id.resolution)
        private val powerView: TextView = view.findViewById(R.id.power)
        private val maxRangeView: TextView = view.findViewById(R.id.max_range)
        private val intTypeView: TextView = view.findViewById(R.id.int_type)
        private val maxDataRateView: TextView = view.findViewById(R.id.max_data_rate)

        fun bind(sensorItem: SensorItem) {
            idView.text = sensorItem.id
            nameView.text = sensorItem.name
            versionView.text = sensorItem.version
            vendorView.text = sensorItem.vendor
            typeView.text = sensorItem.type
            resolutionView.text = sensorItem.resolution
            powerView.text = sensorItem.power
            maxRangeView.text = sensorItem.maxRange
            intTypeView.text = sensorItem.intType
            maxDataRateView.text = sensorItem.maxDataRate
        }
    }
}
