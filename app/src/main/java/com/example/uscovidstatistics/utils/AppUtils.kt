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

    private fun setLocationData(address: List<Address>): LocationDataset {
        val locationDataSet = LocationDataset()
        locationDataSet.city = address[0].locality
        locationDataSet.region = address[0].adminArea
        locationDataSet.county = address[0].countryName
        locationDataSet.postalCode = address[0].postalCode
        locationDataSet.knownName = address[0].featureName
        return locationDataSet
    }

    fun getCurrentData(designation: Int): Any {
        return when (designation) {
            0 -> MathUtils().totalGlobalCases()
            1 -> AppConstants.US_DATA
            2 -> AppConstants.US_STATE_DATA
            3 -> AppConstants.CONTINENT_DATA
            4 -> AppConstants.COUNTRY_DATA
            5 ->  AppConstants.COUNTRY_PROVINCE_DATA
            else -> AppConstants.WORLD_DATA
        }
    }
}