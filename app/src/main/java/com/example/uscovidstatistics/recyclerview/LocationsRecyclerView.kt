package com.example.uscovidstatistics.recyclerview

import android.content.Context
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.uscovidstatistics.R
import com.example.uscovidstatistics.model.apidata.BaseCountryDataset
import com.example.uscovidstatistics.views.activities.homepage.MainActivity

class LocationsRecyclerView(private val mContext: Context, private val activity: MainActivity)  {

    private lateinit var adapterLRV: LocationsRecyclerViewAdapter

    private lateinit var recyclerView : RecyclerView

    private lateinit var recyclerData: List<BaseCountryDataset>

    fun displaySavedLocations() {
        recyclerView = activity.findViewById(R.id.recycler_view)
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(mContext)
        recyclerData = activity.getSavedLocations()
        adapterLRV = LocationsRecyclerViewAdapter(recyclerData)
        recyclerView.recycledViewPool.setMaxRecycledViews(0,0)
        recyclerView.adapter = adapterLRV
        adapterLRV.notifyDataSetChanged()
    }
}