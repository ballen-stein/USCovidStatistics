package com.example.uscovidstatistics.recyclerview

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.example.uscovidstatistics.databinding.SavedLocationsLayoutBinding
import com.example.uscovidstatistics.model.apidata.BaseCountryDataset
import com.example.uscovidstatistics.utils.AppUtils
import kotlinx.android.synthetic.main.saved_locations_layout.view.*

class LocationsRecyclerViewAdapter internal constructor(private val savedCountriesList: List<BaseCountryDataset>) : RecyclerView.Adapter<LocationsRecyclerViewAdapter.ViewHolder>(), ViewBinding {

    private val appUtils = AppUtils.getInstance()

    private lateinit var binding: SavedLocationsLayoutBinding

    private lateinit var savedLocationCountry: String

    private lateinit var savedLocationCases: String

    private lateinit var savedLocationRecovered: String

    private lateinit var savedLocationDeaths: String

    private lateinit var savedLocationInfected: String

    private lateinit var savedLocationMild: String

    private lateinit var savedLocationCritical: String

    private lateinit var savedLocationClosed: String

    private lateinit var savedLocationDischarged: String

    private lateinit var savedLocationDead: String


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentData = savedCountriesList[position]
        setSavedLocationData(currentData)
        holder.bind(currentData)

        holder.itemView.sl_header.text = savedLocationCountry

        holder.itemView.sl_total_cases.text = savedLocationCases
        holder.itemView.sl_recovered.text = savedLocationRecovered
        holder.itemView.sl_deaths.text = savedLocationDeaths

        holder.itemView.sl_infected.text = savedLocationInfected
        holder.itemView.sl_mild.text = savedLocationMild
        holder.itemView.sl_critical.text = savedLocationCritical

        holder.itemView.sl_closed.text = savedLocationClosed
        holder.itemView.sl_discharged.text = savedLocationDischarged
        holder.itemView.sl_dead.text = savedLocationDead
    }

    private fun setSavedLocationData(currentData: BaseCountryDataset) {
        savedLocationCountry = if (currentData.country!! == "USA") "United States Information" else if (currentData.country!! == "UK") "United Kingdom Information" else "${currentData.country!!} Information"
        savedLocationCases = appUtils.formatNumbers(currentData.cases!!)
        savedLocationRecovered = appUtils.formatNumbers(currentData.recovered!!)
        savedLocationDeaths = appUtils.formatNumbers(currentData.deaths!!)

        savedLocationInfected = "${appUtils.formatNumbers(currentData.activeCases!!)} (${appUtils.getStringPercent(currentData.activeCases!!, currentData.cases!!)}%)"

        val mild = (currentData.activeCases!! - currentData.criticalCases!!)
        savedLocationMild = "${appUtils.formatNumbers(mild)} (${appUtils.getStringPercent(mild, currentData.activeCases!!)}%)"
        savedLocationCritical = "${appUtils.formatNumbers(currentData.criticalCases!!)} (${appUtils.getStringPercent(currentData.criticalCases!!, currentData.activeCases!!)}%)"

        val closed = currentData.cases!! - currentData.activeCases!!
        savedLocationClosed = "${appUtils.formatNumbers(closed)} (${appUtils.getStringPercent(closed, currentData.cases!!)}%)"
        val discharged = closed - currentData.deaths!!
        savedLocationDischarged = "${appUtils.formatNumbers(discharged)} (${appUtils.getStringPercent(discharged, closed)}%)"
        savedLocationDead = "${appUtils.formatNumbers(currentData.deaths!!)} (${appUtils.getStringPercent(currentData.deaths!!, closed)}%)"
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding = SavedLocationsLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return savedCountriesList.size
    }

    class ViewHolder(binding: SavedLocationsLayoutBinding) : RecyclerView.ViewHolder(binding.root) {
        private lateinit var currentData: BaseCountryDataset

        fun bind(baseDataset: BaseCountryDataset) {
            currentData = baseDataset
        }

    }

    override fun getRoot(): View {
        return binding.root
    }
}