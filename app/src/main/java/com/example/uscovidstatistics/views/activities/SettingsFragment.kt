package com.example.uscovidstatistics.views.activities

import android.app.Activity
import android.os.Bundle
import android.util.Log
import androidx.preference.ListPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreferenceCompat
import com.example.uscovidstatistics.R
import com.example.uscovidstatistics.appconstants.AppConstants
import com.example.uscovidstatistics.utils.AppUtils
import com.example.uscovidstatistics.utils.PreferenceUtils
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subjects.PublishSubject
import java.lang.Exception
import java.util.*

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
            Log.d(
                "CovidTesting",
                "${preference.title} has a value of $newValue and a key of ${preference!!.key}"
            )
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