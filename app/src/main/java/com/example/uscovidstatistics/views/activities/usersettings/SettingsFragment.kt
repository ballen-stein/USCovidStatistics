package com.example.uscovidstatistics.views.activities.usersettings

import android.app.Activity
import android.os.Bundle
import androidx.preference.*
import com.example.uscovidstatistics.R
import com.example.uscovidstatistics.appconstants.AppConstants
import com.example.uscovidstatistics.utils.AppUtils
import com.example.uscovidstatistics.utils.PreferenceUtils
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashSet

class SettingsFragment(private val mActivity: Activity) : PreferenceFragmentCompat() {

    private lateinit var prefUtils: PreferenceUtils

    private lateinit var appUtils: AppUtils

    private val settingsTimer = Timer()

    private lateinit var countryList: List<String>

    private var listWithData = ArrayList<String>()

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences, rootKey)
        prefUtils = PreferenceUtils.getInstance(mActivity)
        appUtils = AppUtils.getInstance()

        setPrefListeners()
    }

    private fun setPrefListeners() {
        findPreference<SwitchPreferenceCompat>(getString(R.string.preference_gps))!!.setOnPreferenceChangeListener { _, newValue ->
            if (appUtils.gpsPermissionGranted(mActivity)) {
                prefUtils.prefSaveGps(newValue as Boolean)
                true
            } else {
                appUtils.checkLaunchPermissions(mActivity)
                settingsTimer.schedule(object : TimerTask() {
                    override fun run() {
                        if (AppConstants.Settings_Updated) {
                            updateSettings()
                            this.cancel()
                        }
                    }
                },0, 500)
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
            countryList = appUtils.getNotificationCountries(mActivity.applicationContext)
            if (countryList.isNotEmpty()) {
                setCountryNotificationData()
                val savedNotifLocations: Set<String> = HashSet<String>(listWithData)
                prefUtils.prefSaveNotifications(getString(R.string.preference_notif_locations), savedNotifLocations)
            }
            true
        }

        findPreference<CheckBoxPreference>(getString(R.string.preference_notif_recovered))!!.setOnPreferenceChangeListener { preference, newValue ->
            prefUtils.prefSaveNotifications(preference.key, newValue as Boolean)
            countryList = appUtils.getNotificationCountries(mActivity.applicationContext)
            if (countryList.isNotEmpty()) {
                setCountryNotificationData()
                val savedNotifLocations: Set<String> = HashSet<String>(listWithData)
                prefUtils.prefSaveNotifications(getString(R.string.preference_notif_locations), savedNotifLocations)
            }
            true
        }

        findPreference<CheckBoxPreference>(getString(R.string.preference_notif_deaths))!!.setOnPreferenceChangeListener { preference, newValue ->
            prefUtils.prefSaveNotifications(preference.key, newValue as Boolean)
            countryList = appUtils.getNotificationCountries(mActivity.applicationContext)
            if (countryList.isNotEmpty()) {
                setCountryNotificationData()
                val savedNotifLocations: Set<String> = HashSet<String>(listWithData)
                prefUtils.prefSaveNotifications(getString(R.string.preference_notif_locations), savedNotifLocations)
            }
            true
        }

        findPreference<EditTextPreference>(getString(R.string.preference_notif_locations))!!.setOnPreferenceChangeListener { preference, newValue ->
            if ((newValue as String).isNotBlank()) {
                countryList = newValue.split(",", ignoreCase = true, limit = 3).toMutableList()
                setCountryNotificationData()
                val savedNotifLocations: Set<String> = HashSet<String>(listWithData)
                prefUtils.prefSaveNotifications(preference.key, savedNotifLocations)
            } else {
                prefUtils.prefSaveNotifications(preference.key, HashSet<String>())
            }
            true
        }

        findPreference<ListPreference>(getString(R.string.preference_notification_frequency))!!.setOnPreferenceChangeListener { _, newValue ->
            prefUtils.prefSaveNotificationFreq((newValue as String).toLong())
            true
        }
    }

    private fun updateSettings() {
        mActivity.runOnUiThread {
            findPreference<SwitchPreferenceCompat>(getString(R.string.preference_gps))!!.isChecked = true
        }
    }

    private fun setCountryNotificationData() {
        for (value in countryList) {
            val tempVal = if (value.contains(",")) {
                value.split(",")[0].trim()
            } else {
                value.trim()
            }
            listWithData.add("${tempVal}/${appUtils.createHigherNotificationNumber(AppConstants.World_Data_Mapped[tempVal]!!.cases!!)}/false" +
                    "/${appUtils.createHigherNotificationNumber(AppConstants.World_Data_Mapped[tempVal]!!.recovered!!)}/false" +
                    "/${appUtils.createHigherNotificationNumber(AppConstants.World_Data_Mapped[tempVal]!!.deaths!!)}/false")
        }
    }

    override fun onDestroy() {
        settingsTimer.cancel()
        super.onDestroy()
    }
}