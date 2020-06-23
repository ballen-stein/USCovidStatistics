package com.example.uscovidstatistics.views.activities

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import com.example.uscovidstatistics.R

class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        //addPreferencesFromResource(R.xml.preferences)
        setPreferencesFromResource(R.xml.preferences, rootKey)
    }
}