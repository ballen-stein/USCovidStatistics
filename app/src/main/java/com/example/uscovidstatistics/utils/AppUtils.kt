package com.example.uscovidstatistics.utils

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.uscovidstatistics.appconstants.AppConstants
import com.example.uscovidstatistics.model.LocationDataset
import java.math.BigDecimal
import java.math.RoundingMode
import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.*

class AppUtils {

    fun checkLaunchPermissions(context: Context): Boolean {
        return if (!gpsPermissionGranted(context)) {
            ActivityCompat.requestPermissions(
                context as Activity,
                arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION),
                AppConstants.REQUEST_GPS_LOCATION
            )
            false
        } else {
            true
        }
    }

    fun gpsPermissionGranted(context: Context): Boolean {
        return ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
    }

    fun getLocationData(context: Context): LocationDataset {
        val geocoder = Geocoder(context, Locale.getDefault())
        val address: List<Address> = geocoder.getFromLocation(AppConstants.GPS_DATA[1], AppConstants.GPS_DATA[0], 1)
        return setLocationData(address)
    }

    fun totalGlobalCases(): IntArray {
        var cases = 0
        var recovered = 0
        var deaths = 0
        var activeCases = 0
        var critical = 0

        for (data in AppConstants.CONTINENT_DATA) {
            cases += data.cases!!
            recovered += data.recovered!!.toInt()
            deaths += data.deaths!!
            activeCases += data.activeCases!!
            critical += data.criticalCases!!
        }
        val mild = activeCases - critical
        val closedCases = cases - activeCases

        return intArrayOf(cases, recovered, deaths, activeCases, mild, critical, closedCases)
    }

    fun formatNumbers(num: Int): String {
        return if (num.toString().length <= 3) {
            num.toString()
        } else {
            NumberFormat.getInstance().format(num.toDouble())
        }
    }

    fun getPercent(num1: Int, num2: Int): Double {
        return (num1.toDouble() / num2.toDouble()) * 100
    }

    fun getStringPercent(num1: Int, num2: Int): String {
        val percent = getPercent(num1, num2)
        return BigDecimal(percent.toString()).setScale(2, BigDecimal.ROUND_HALF_EVEN).toString()
    }

    private fun setLocationData(address: List<Address>): LocationDataset {
        val locationDataSet = LocationDataset()
        locationDataSet.city = address[0].locality
        locationDataSet.region = address[0].adminArea
        locationDataSet.county = address[0].countryName
        locationDataSet.postalCode = address[0].postalCode
        locationDataSet.knownName = address[0].featureName
        return locationDataSet
    }
}