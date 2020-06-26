package com.example.uscovidstatistics.views.activities.usersettings

import android.app.Activity
import android.os.Bundle
import androidx.preference.*
import com.example.uscovidstatistics.R
import com.example.uscovidstatistics.appconstants.AppConstants
import com.example.uscovidstatistics.utils.AppUtils
import com.example.uscovidstatistics.utils.PreferenceUtils
import java.util.*
import kotlin.collections.HashSet

class SettingsFragment(private val mActivity: Activity) : PreferenceFragmentCompat() {

    private lateinit var prefUtils: PreferenceUtils

    private val settingsTimer = Timer()

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        //addPreferencesFromResource(R.xml.preferences)
        setPreferencesFromResource(R.xml.preferences, rootKey)
        prefUtils = PreferenceUtils.getInstance(mActivity)

        setPrefListeners()
    }

    private fun setPrefListeners() {
        findPreference<SwitchPreferenceCompat>(getString(R.string.preference_gps))!!.setOnPreferenceChangeListener { _, newValue ->
            if (AppUtils.getInstance().gpsPermissionGranted(mActivity)) {
                prefUtils.prefSaveGps(newValue as Boolean)
                true
            } else {
                AppUtils.getInstance().checkLaunchPermissions(mActivity)
                settingsTimer.schedule(object : TimerTask() {
                    override fun run() {
                        if (AppConstants.Settings_Updated) {
                            updateSettings()
                            this.cancel()
                        }
                    }
                },0, 1000)
                false
            }
        }

        findPreference<ListPreference>(getString(R.string.preference_frequency))!!.setOnPreferenceChangeListener { _, newValue ->
            prefUtils.prefSaveFrequency((newValue as String).toLong())
            true
        }

        findPreference<SwitchPreferenceCompat>(getString(R.string.preference_notifications))!!.setOnPreferenceChangeListener { preference, newValue ->
            prefUtils.prefSaveNotifications(preference.key, newValue as Boolean)
            true
        }

        findPreference<CheckBoxPreference>(getString(R.string.preference_notif_cases))!!.setOnPreferenceChangeListener { preference, newValue ->
            prefUtils.prefSaveNotifications(preference.key, newValue as Boolean)
            true
        }

        findPreference<CheckBoxPreference>(getString(R.string.preference_notif_recovered))!!.setOnPreferenceChangeListener { preference, newValue ->
            prefUtils.prefSaveNotifications(preference.key, newValue as Boolean)
            true
        }

        findPreference<CheckBoxPreference>(getString(R.string.preference_notif_deaths))!!.setOnPreferenceChangeListener { preference, newValue ->
            prefUtils.prefSaveNotifications(preference.key, newValue as Boolean)
            true
        }

        findPreference<EditTextPreference>(getString(R.string.preference_notif_locations))!!.setOnPreferenceChangeListener { preference, newValue ->
            if ((newValue as String).isNotBlank()) {
                val countryList = newValue.split(",", ignoreCase = true, limit = 3).toMutableList()
                for ((i,value) in countryList.withIndex()) {
                    if (value.contains(",")) {
                        val tempVal = value.split(",")[0].trim()
                        countryList[i] = tempVal
                    } else {
                        val tempVal = value.trim()
                        countryList[i] = tempVal
                    }
                }
                val savedNotifLocations: Set<String> = HashSet<String>(countryList)
                prefUtils.prefSaveNotifications(preference.key, savedNotifLocations)
            } else {
                prefUtils.prefSaveNotifications(preference.key, HashSet<String>())
            }
            true
        }
    }

    private fun updateSettings() {
        mActivity.runOnUiThread {
            findPreference<SwitchPreferenceCompat>(getString(R.string.preference_gps))!!.isChecked = true
        }
    }

    override fun onDestroy() {
        settingsTimer.cancel()
        super.onDestroy()
    }
}