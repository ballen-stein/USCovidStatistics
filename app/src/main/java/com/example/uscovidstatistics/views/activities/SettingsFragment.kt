package com.example.uscovidstatistics.views.activities

import android.app.Activity
import android.os.Bundle
import android.util.Log
import androidx.preference.ListPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreferenceCompat
import com.example.uscovidstatistics.R
import com.example.uscovidstatistics.utils.PreferenceUtils
import java.lang.Exception

class SettingsFragment(private val mActivity: Activity) : PreferenceFragmentCompat() {

    private lateinit var prefUtils: PreferenceUtils

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        //addPreferencesFromResource(R.xml.preferences)
        setPreferencesFromResource(R.xml.preferences, rootKey)
        prefUtils = PreferenceUtils.getInstance(mActivity)

        setPrefListeners()
    }

    private fun setPrefListeners() {
        findPreference<SwitchPreferenceCompat>(getString(R.string.preference_gps))!!.setOnPreferenceChangeListener { preference, newValue ->
            prefUtils.prefSaveGps(newValue as Boolean)
            true
        }

        findPreference<ListPreference>(getString(R.string.preference_frequency))!!.setOnPreferenceChangeListener { preference, newValue ->
            prefUtils.prefSaveFrequency((newValue as String).toLong())
            true
        }

        findPreference<SwitchPreferenceCompat>(getString(R.string.preference_notifications))!!.setOnPreferenceChangeListener { preference, newValue ->
            Log.d(
                "CovidTesting",
                "${preference.title} has a value of $newValue and a key of ${preference!!.key}"
            )
            true
        }
    }
}