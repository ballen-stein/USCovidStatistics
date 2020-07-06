package com.example.uscovidstatistics.utils

import android.content.Context
import android.util.Log
import com.example.uscovidstatistics.R
import com.example.uscovidstatistics.appconstants.AppConstants

class PreferenceUtils(private val mContext: Context) {

    fun addToSavedList(countryName: String) {
        val sharedPref = mContext.getSharedPreferences(mContext.resources.getString(R.string.app_package), Context.MODE_PRIVATE)
        with (sharedPref.edit()) {
            val savedLocations = getUserLocations()
            if (savedLocations != null) {
                val countryNames = checkIfLocationExists(countryName)
                putString(mContext.getString(R.string.preference_saved_location), countryNames)
            } else {
                putString(mContext.getString(R.string.preference_saved_location), countryName)
            }
            commit()
        }
    }

    fun prefSaveGps(showOnLaunch: Boolean) {
        val sharedPref = mContext.getSharedPreferences(mContext.resources.getString(R.string.app_package), Context.MODE_PRIVATE)
        with (sharedPref.edit()) {
            putBoolean(mContext.getString(R.string.preference_gps), showOnLaunch)
            commit()
        }
    }

    // Allow/Disable Notifications and its specifics: Case/Recovered/Death
    fun prefSaveNotifications(location: String, notificationsOn: Boolean) {
        val sharedPref = mContext.getSharedPreferences(mContext.resources.getString(R.string.app_package), Context.MODE_PRIVATE)
        with (sharedPref.edit()) {
            putBoolean(location, notificationsOn)
            commit()
        }
    }

    // Save/Delete the Location(s) to have notifications for
    fun prefSaveNotifications(location: String, notificationSet: Set<String>) {
        val sharedPref = mContext.getSharedPreferences(mContext.resources.getString(R.string.app_package), Context.MODE_PRIVATE)

        Log.d("CovidTesting", "Final HashSet: $notificationSet")
        with (sharedPref.edit()) {
            putStringSet(location, notificationSet)
            commit()
        }
    }

    fun prefSavedFromNotifications(set: HashSet<String>) {
        val sharedPref = mContext.getSharedPreferences(mContext.resources.getString(R.string.app_package), Context.MODE_PRIVATE)
        with (sharedPref.edit()) {
            putStringSet(mContext.resources.getString(R.string.preference_notif_locations), set)
            commit()
        }
    }

    fun prefSaveFrequency(frequency: Long) {
        val sharedPref = mContext.getSharedPreferences(mContext.resources.getString(R.string.app_package), Context.MODE_PRIVATE)
        with (sharedPref.edit()) {
            putLong(mContext.getString(R.string.preference_frequency), frequency)
            commit()
        }
    }

    fun prefSaveNotificationFreq(frequency: Long) {
        val sharedPref = mContext.getSharedPreferences(mContext.resources.getString(R.string.app_package), Context.MODE_PRIVATE)
        with (sharedPref.edit()) {
            putLong(mContext.getString(R.string.preference_notification_frequency), frequency)
            commit()
        }
    }

    fun baseInit() {
        val sharedPref = mContext.getSharedPreferences(mContext.resources.getString(R.string.app_package), Context.MODE_PRIVATE)
        with (sharedPref.edit()) {
            val savedLocations = getUserLocations()
            if (savedLocations!!.isEmpty()) {
                putString(mContext.getString(R.string.preference_saved_location), "Global")
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
        return mContext.getSharedPreferences(mContext.resources.getString(R.string.app_package), Context.MODE_PRIVATE).getString(mContext.resources.getString(R.string.preference_saved_location), "")
    }

    fun userPreferences() {
        AppConstants.User_Prefs = mContext.getSharedPreferences(mContext.resources.getString(R.string.app_package), Context.MODE_PRIVATE) ?: return
    }

    companion object {
        fun getInstance(mContext: Context): PreferenceUtils {
            return PreferenceUtils(mContext)
        }
    }
}