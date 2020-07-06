package com.example.uscovidstatistics.views.activities.usersettings

import android.app.Activity
import android.os.Bundle
import androidx.preference.*
import com.example.uscovidstatistics.R
import com.example.uscovidstatistics.appconstants.AppConstants
import com.example.uscovidstatistics.manualdependency.DependencyInjectorImpl
import com.example.uscovidstatistics.utils.AppUtils
import com.example.uscovidstatistics.utils.PreferenceUtils
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashSet

class SettingsFragment(private val mActivity: Activity) : PreferenceFragmentCompat() {

    private lateinit var prefUtils: PreferenceUtils

    private lateinit var appUtils: AppUtils

    private val settingsTimer = Timer()

    private var countryList = ArrayList<String>()

    private var listWithData = ArrayList<String>()

    private val dataModel = DependencyInjectorImpl().covidDataRepository()

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences, rootKey)
        prefUtils = PreferenceUtils.getInstance(mActivity)
        appUtils = AppUtils.getInstance()

        val screen = this.preferenceScreen
        val listPrefLocation = screen.findPreference<PreferenceCategory>(getString(R.string.preference_title_notifications_key))!!
        val listPref = setCountryList(screen)
        listPrefLocation.addPreference(listPref)
        setPrefListeners()
    }

    private fun setCountryList(screen: PreferenceScreen): MultiSelectListPreference {
        val mappedValues = dataModel.getMappedWorldData()
        val nonCountries = resources.getStringArray(R.array.territories_list)
        for (countryName in nonCountries) {
            if (mappedValues.contains(countryName)) {
                mappedValues.remove(countryName)
            }
        }


        val listVals = mappedValues.keys.toTypedArray()
        listVals.sort(0, listVals.size-1)

        val emptySet = HashSet<String>()

        val prefMultiList = MultiSelectListPreference(screen.context)
        prefMultiList.apply {
            key = getString(R.string.preference_notif_locations)
            title = getString(R.string.preference_notif_add_location)
            summary = getString(R.string.preference_notification_desc)
            entryValues = listVals
            entries = listVals
            isEnabled = AppConstants.User_Prefs.getBoolean(getString(R.string.preference_notifications), false)
            setDefaultValue(emptySet)
        }

        return prefMultiList
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
            findPreference<MultiSelectListPreference>(getString(R.string.preference_notif_locations))!!.isEnabled = newValue
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

        findPreference<MultiSelectListPreference>(getString(R.string.preference_notif_locations))!!.setOnPreferenceChangeListener { preference, newValue ->
            // Unchecked but value will always be a string
            newValue as Set<String>

            if ((newValue).isNotEmpty()) {
                countryList.addAll(newValue)
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
            val tempVal = value.trim()
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