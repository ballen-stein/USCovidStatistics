package com.example.uscovidstatistics.recyclerview

import android.app.Activity
import android.content.Intent
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.uscovidstatistics.R
import com.example.uscovidstatistics.appconstants.AppConstants
import com.example.uscovidstatistics.utils.AppUtils
import com.example.uscovidstatistics.views.activities.country.CountryActivity
import com.example.uscovidstatistics.views.activities.homepage.MainActivity
import com.example.uscovidstatistics.views.activities.state.StateActivity
import com.example.uscovidstatistics.views.activities.usersettings.UserSettings
import com.example.uscovidstatistics.views.dialogs.CountryListDialog

class NavRecyclerView(private val mContext: Activity, private val countryListDialog: CountryListDialog, private val navigationRecycler: RecyclerView) {
    private lateinit var adapterNavView : NavRecyclerViewAdapter
    private lateinit var recyclerView : RecyclerView
    private lateinit var recyclerData : Map<String, Array<String>>

    fun displayChoices(choice: String){
        recyclerView = navigationRecycler
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(mContext.applicationContext)
        recyclerData = countryListDialog.getContinentList()
        adapterNavView = NavRecyclerViewAdapter(recyclerData.getValue(choice))
        recyclerView.recycledViewPool.setMaxRecycledViews(0,0)
        recyclerView.adapter = adapterNavView
        adapterNavView.notifyDataSetChanged()
        setListener()
    }

    private fun setListener(){
        adapterNavView.setOnClickListener(object : NavRecyclerViewAdapter.OnClickListener {
            override fun onCountryClick(position: Int, countryName: String, v: View) {
                if (AppConstants.Recycler_Clickable) {
                    val actualCountry = AppUtils.getInstance().territoriesDirectLink(countryName, mContext)

                    val intent = Intent(mContext.applicationContext, CountryActivity::class.java)
                    if (actualCountry != "null")
                        intent.putExtra(AppConstants.Display_Country, actualCountry)
                    else
                        intent.putExtra(AppConstants.Display_Country, countryName)

                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                    countryListDialog.dismiss()

                    if (mContext is MainActivity) {
                        mContext.startActivity(intent)
                        mContext.overridePendingTransition(R.anim.enter_right, R.anim.exit_left)
                    } else if (mContext is UserSettings || mContext is StateActivity) {
                        mContext.startActivity(intent)
                        mContext.overridePendingTransition(R.anim.enter_right, R.anim.exit_left)
                        mContext.finish()
                    } else {
                        if (actualCountry != "null")
                            mContext.intent.putExtra(AppConstants.Display_Country, actualCountry)
                        else
                            mContext.intent.putExtra(AppConstants.Display_Country, countryName)
                        mContext.recreate()
                    }
                }
            }
        })
    }
}