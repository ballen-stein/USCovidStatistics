package com.example.uscovidstatistics.recyclerview

import android.content.Context
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.uscovidstatistics.R
import com.example.uscovidstatistics.model.CleanedUpData
import com.example.uscovidstatistics.views.activities.country.CountryActivity

class CleanedDataRecyclerView (private val mContext: Context, private val activity: CountryActivity) {
    private lateinit var adapterCleanedData : CleanedDataRecyclerViewAdapter
    private lateinit var recyclerView : RecyclerView
    private lateinit var recyclerData : List<CleanedUpData>

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
            override fun onRepoClick(position: Int, cleanedUpData: CleanedUpData, v: View) {
                //activity
            }
        })
    }
}