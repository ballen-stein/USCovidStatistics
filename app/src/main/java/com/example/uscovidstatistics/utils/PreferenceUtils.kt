package com.example.uscovidstatistics.utils

import android.app.Activity
import android.content.Context
import android.util.Log
import com.example.uscovidstatistics.R
import com.example.uscovidstatistics.appconstants.AppConstants

class PreferenceUtils(private val mActivity: Activity) {

    fun addToSavedList(countryName: String) {
        val sharedPref = mActivity.getSharedPreferences(mActivity.resources.getString(R.string.app_package), Context.MODE_PRIVATE)
        with (sharedPref.edit()) {
            val savedLocations = getUserLocations()
            if (savedLocations != null) {
                val countryNames = checkIfLocationExists(countryName)
                putString(mActivity.getString(R.string.preference_saved_location), countryNames)
            } else {
                putString(mActivity.getString(R.string.preference_saved_location), countryName)
            }
            commit()
        }
    }

    fun prefSaveGps(showOnLaunch: Boolean) {
        val sharedPref = mActivity.getSharedPreferences(mActivity.resources.getString(R.string.app_package), Context.MODE_PRIVATE)
        with (sharedPref.edit()) {
            putBoolean(mActivity.getString(R.string.preference_gps), showOnLaunch)
            commit()
        }
    }

    // Allow/Disable Notifications and its specifics: Case/Recovered/Death
    fun prefSaveNotifications(location: String, notificationsOn: Boolean) {
        val sharedPref = mActivity.getSharedPreferences(mActivity.resources.getString(R.string.app_package), Context.MODE_PRIVATE)
        with (sharedPref.edit()) {
            putBoolean(location, notificationsOn)
            commit()
        }
    }

    // Save/Delete the Location(s) to have notifications for
    fun prefSaveNotifications(location: String, countryNameList: Set<String>) {
        val sharedPref = mActivity.getSharedPreferences(mActivity.resources.getString(R.string.app_package), Context.MODE_PRIVATE)
        with (sharedPref.edit()) {
            putStringSet(location, countryNameList)
            commit()
        }
    }

    fun prefSaveFrequency(frequency: Long) {
        val sharedPref = mActivity.getSharedPreferences(mActivity.resources.getString(R.string.app_package), Context.MODE_PRIVATE)
        with (sharedPref.edit()) {
            putLong(mActivity.getString(R.string.preference_frequency), frequency)
            commit()
        }
    }

    fun baseInit() {
        val sharedPref = mActivity.getSharedPreferences(mActivity.resources.getString(R.string.app_package), Context.MODE_PRIVATE)
        with (sharedPref.edit()) {
            val savedLocations = getUserLocations()
            if (savedLocations!!.isEmpty()) {
                putString(mActivity.getString(R.string.preference_saved_location), "Global")
                commit()
            }
        }
    }

    fun checkPref(countryName: String): Boolean {
        return getUserLocations()!!.split("/").contains(countryName)
    }

    private fun checkIfLocationExists(countryName: String): String {
        val prefs = getUserLocations()!!.split("/")
        return if (prefs.contains(countryName)) {
            val tempList: ArrayList<String> = ArrayList()
            tempList.addAll(prefs)
            tempList.remove(countryName)
            tempList.joinToString("/")
        } else {
            "${getUserLocations()}/$countryName"
        }
    }

    private fun getUserLocations(): String? {
        return mActivity.getSharedPreferences(mActivity.resources.getString(R.string.app_package), Context.MODE_PRIVATE).getString(mActivity.resources.getString(R.string.preference_saved_location), "")
    }

    fun userPreferences() {
        AppConstants.User_Prefs = mActivity.getSharedPreferences(mActivity.resources.getString(R.string.app_package), Context.MODE_PRIVATE) ?: return
    }

    companion object {
        fun getInstance(mActivity: Activity): PreferenceUtils {
            return PreferenceUtils(mActivity)
        }
    }
}