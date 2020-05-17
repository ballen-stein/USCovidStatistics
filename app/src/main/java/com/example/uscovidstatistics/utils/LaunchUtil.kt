package com.example.uscovidstatistics.utils

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.uscovidstatistics.appconstants.AppConstants

class LaunchUtil(context: Context, activity: Activity) {
    private val mContext: Context = context
    private val mActivity: Activity = activity

    fun checkPermissions(): Boolean {
        return if (ContextCompat.checkSelfPermission(
                mContext,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                mActivity,
                arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION),
                AppConstants.REQUEST_GPS_LOCATION
            )
            false
        } else {
            true
        }
    }
}