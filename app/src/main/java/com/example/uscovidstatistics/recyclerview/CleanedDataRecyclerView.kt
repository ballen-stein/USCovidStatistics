package com.example.uscovidstatistics.recyclerview

import android.content.Context
import android.content.Intent
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.uscovidstatistics.R
import com.example.uscovidstatistics.appconstants.AppConstants
import com.example.uscovidstatistics.model.CleanedUpData
import com.example.uscovidstatistics.views.activities.country.CountryActivity
import com.example.uscovidstatistics.views.activities.region.StateActivity
import kotlinx.android.synthetic.main.activity_country_breakdown.view.*

class CleanedDataRecyclerView (private val mContext: Context, private val activity: CountryActivity) {
    private lateinit var adapterCleanedData: CleanedDataRecyclerViewAdapter

    private lateinit var recyclerView: RecyclerView

    private lateinit var recyclerData: List<CleanedUpData>

    fun displayCleanedData(){
        recyclerView = activity.findViewById(R.id.recycler_view)
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(mContext)
        recyclerData = activity.getCleanedUpData()
        adapterCleanedData = CleanedDataRecyclerViewAdapter(recyclerData)
        recyclerView.recycledViewPool.setMaxRecycledViews(0,0)
        recyclerView.adapter = adapterCleanedData
        adapterCleanedData.notifyDataSetChanged()
        setListener()
    }

    private fun setListener(){
        adapterCleanedData.setOnClickListener(object : CleanedDataRecyclerViewAdapter.OnClickListener {
            override fun onRegionClick(position: Int, cleanedUpData: CleanedUpData, v: View) {
                val intent = Intent(mContext, StateActivity::class.java)
                val countryName = activity.root.cases_header.text.split(" Information")[0]

                intent.putExtra(AppConstants.DISPLAY_REGION, cleanedUpData.name)
                    .putExtra(AppConstants.DISPLAY_COUNTRY, countryName)
                mContext.startActivity(intent)
                activity.overridePendingTransition(R.anim.enter_right, R.anim.exit_left)

            }
        })
    }
}