package com.example.uscovidstatistics.recyclerview

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.uscovidstatistics.R
import com.example.uscovidstatistics.appconstants.AppConstants
import com.example.uscovidstatistics.model.apidata.BaseCountryDataset
import com.example.uscovidstatistics.views.activities.country.CountryActivity
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
        setListener()
    }

    private fun setListener() {
        adapterLRV.setOnClickListener(object : LocationsRecyclerViewAdapter.OnClickListener {
            override fun openToLocation(currentData: BaseCountryDataset, view: View?) {
                val intent = Intent(mContext, CountryActivity::class.java)
                intent.putExtra(AppConstants.Display_Country, currentData.country)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                mContext.startActivity(intent)
                activity.overridePendingTransition(R.anim.enter_right, R.anim.exit_left)
            }
        })
    }
}