package com.example.uscovidstatistics.recyclerview

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.uscovidstatistics.R
import com.example.uscovidstatistics.appconstants.AppConstants
import com.example.uscovidstatistics.views.activities.country.CountryActivity
import com.example.uscovidstatistics.views.activities.homepage.MainActivity
import com.example.uscovidstatistics.views.dialogs.BottomDialog

class NavRecyclerView(private val mContext: Context, private val bottomDialog: BottomDialog, private val navigationRecycler: RecyclerView) {
    private lateinit var adapterNavView : NavRecyclerViewAdapter
    private lateinit var recyclerView : RecyclerView
    private lateinit var recyclerData : Map<String, Array<String>>

    fun displayChoices(choice: String){
        recyclerView = navigationRecycler
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(mContext)
        recyclerData = bottomDialog.getContinentList()
        adapterNavView = NavRecyclerViewAdapter(recyclerData[choice]!!)
        recyclerView.recycledViewPool.setMaxRecycledViews(0,0)
        recyclerView.adapter = adapterNavView
        adapterNavView.notifyDataSetChanged()
        setListener()
    }

    private fun setListener(){
        adapterNavView.setOnClickListener(object : NavRecyclerViewAdapter.OnClickListener {
            override fun onCountryClick(position: Int, countryName: String, v: View) {
                val intent = if (!countryName.equals("USA")) {
                    Intent(mContext, CountryActivity::class.java)
                } else {
                    Intent(mContext, MainActivity::class.java)
                }
                intent.putExtra(AppConstants.DISPLAY_COUNTRY, countryName)

                //mContext.startActivity(intent)
            }
        })
    }
}