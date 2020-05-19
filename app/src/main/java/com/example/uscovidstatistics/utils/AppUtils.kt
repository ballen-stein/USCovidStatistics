package com.example.uscovidstatistics.utils

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.uscovidstatistics.appconstants.AppConstants
import com.example.uscovidstatistics.model.LocationDataSet
import com.google.android.gms.location.LocationServices
import java.util.*

class AppUtils {

    fun checkLaunchPermissions(context: Context): Boolean {
        return if (checkPermission(context)) {
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

    fun checkPermission(context: Context): Boolean {
        return ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
    }

    fun getLocationData(context: Context): LocationDataSet {
        val geocoder = Geocoder(context, Locale.getDefault())
        val address: List<Address> = geocoder.getFromLocation(AppConstants.GPS_DATA[1], AppConstants.GPS_DATA[0], 1)
        return setLocationData(address)
    }

    private fun setLocationData(address: List<Address>): LocationDataSet {
        val locationDataSet = LocationDataSet()
        locationDataSet.city = address[0].locality
        locationDataSet.region = address[0].adminArea
        locationDataSet.county = address[0].countryName
        locationDataSet.postalCode = address[0].postalCode
        locationDataSet.knownName = address[0].featureName
        return locationDataSet
    }

    fun getGpsLocation(context: Context): Boolean {
        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
        fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
            if (location != null) {
                Log.d("LocationInfo", "App Constant" + location.toString())
                AppConstants.GPS_DATA[0] = location.longitude
                AppConstants.GPS_DATA[1] = location.latitude
            }
        }
        return true
    }

}