package com.example.uscovidstatistics.views.activities

import android.Manifest
import android.content.Intent
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.uscovidstatistics.MainActivity
import com.example.uscovidstatistics.R
import com.example.uscovidstatistics.appconstants.AppConstants
import com.example.uscovidstatistics.model.LocationDataSet
import com.example.uscovidstatistics.utils.AppUtils
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import es.dmoral.toasty.Toasty
import java.lang.Exception

class Splash : AppCompatActivity() {
    private lateinit var loadGpsIntent: Intent
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        loadGpsIntent = Intent(this, MainActivity::class.java)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        if (AppUtils().checkLaunchPermissions(this)) {
            setGpsCoords()
        }
    }

    private fun setGpsCoords() {
        fusedLocationClient.lastLocation.addOnSuccessListener {
                location: Location? -> if (location != null) {
            loadGpsIntent.putExtra(
                AppConstants.CURRENT_GPS_LOCATION,
                doubleArrayOf(location.longitude, location.latitude)
            )
            AppConstants.GPS_DATA[0] = location.longitude
            AppConstants.GPS_DATA[1] = location.latitude
        }
            setLocation()
        }
    }

    private fun setLocation() {
        try {
            Log.d("LocationInfo", "${AppConstants.GPS_DATA[0]} x ${AppConstants.GPS_DATA[1]}")
            AppConstants.LOCATION_DATA = AppUtils().getLocationData(this)
            Log.d("LocationInfo", AppConstants.LOCATION_DATA.city.toString())
        } catch (e: Exception) {
            Log.d("LocationInfo", "No location found")
            e.printStackTrace()
        }
        continueToLaunch()
    }


    private fun continueToLaunch() {
        startActivity(loadGpsIntent)
        finish()
    }

    @Override
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        val permissionMap = HashMap<String, Int>()
        for ((i, perm) in permissions.withIndex())
            permissionMap[perm] = grantResults[i]

        if (requestCode == AppConstants.REQUEST_GPS_LOCATION && permissionMap[Manifest.permission.ACCESS_COARSE_LOCATION] != 0) {
            continueToLaunch()
        } else {
            Toasty.info(this, "GPS Permission granted", Toast.LENGTH_SHORT).show()
            setGpsCoords()
        }
    }
}
