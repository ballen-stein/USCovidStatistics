package com.example.uscovidstatistics.utils

import android.Manifest
import android.app.Activity
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.os.Build
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.example.uscovidstatistics.R
import com.example.uscovidstatistics.appconstants.AppConstants
import com.example.uscovidstatistics.model.CleanedUpData
import com.example.uscovidstatistics.model.LocationDataset
import com.example.uscovidstatistics.model.apidata.ContinentDataset
import com.example.uscovidstatistics.model.apidata.JhuProvinceDataset
import java.math.BigDecimal
import java.text.NumberFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

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
        return ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    fun getLocationData(context: Context): LocationDataset {
        val geocoder = Geocoder(context, Locale.getDefault())
        val address: List<Address> =
            geocoder.getFromLocation(AppConstants.GPS_DATA[1], AppConstants.GPS_DATA[0], 1)
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

    fun newNotification(context: Context): NotificationCompat.Builder? {
        createNotificationChannel(context)
        return NotificationCompat.Builder(context, AppConstants.CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_virus)
            .setContentTitle("Covid-19 Update")
            .setContentText("Current cases : ${AppUtils().totalGlobalCases()[0]}")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
    }

    private fun createNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Channel_name_001"
            val descriptionText = "Channel_text_001"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(AppConstants.CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }

            val notificationManager: NotificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)

        }
    }

    fun setTimerDelay(): Long {
        val currentTime = Calendar.getInstance().time
        val formattedTime = currentTime.toString().split(" ")[3]
        return AppUtils().findDelay(formattedTime)
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

    private var totalsArray = intArrayOf(0,0,0)

    fun cleanCountryData(data: JhuProvinceDataset): CleanedUpData {
        val cleanedUpData = CleanedUpData()
        val keysList: ArrayList<String> = ArrayList()
        val setOfKeys = data.timeline!!.cases!!.keys
        for (key in setOfKeys)
            keysList.add(key)

        val cases = data.timeline!!.cases!![(keysList[keysList.size-1])]
        val recovered = data.timeline!!.recovered!![keysList[keysList.size-1]]
        val deaths = data.timeline!!.deaths!![(keysList[keysList.size-1])]

        totalsArray[0] += cases!!.toInt()
        totalsArray[1] += recovered!!.toInt()
        totalsArray[2] += deaths!!.toInt()

        cleanedUpData.name = capitalizeWords(data.province.toString())
        cleanedUpData.cases = formatNumbers(cases)
        cleanedUpData.recovered = formatNumbers(recovered)
        cleanedUpData.deaths = formatNumbers(deaths)

        return cleanedUpData
    }

    fun capitalizeWords(title: String): String {
        return title.split(" ").joinToString(" \n") { it.capitalize() }
    }

    fun cleanCountryTotals(): CleanedUpData {
        val totalCleaned = CleanedUpData()
        totalCleaned.name = "Totals"
        totalCleaned.cases = formatNumbers(totalsArray[0])
        totalCleaned.recovered = formatNumbers(totalsArray[1])
        totalCleaned.deaths = formatNumbers(totalsArray[2])

        return totalCleaned
    }

    fun resetCountryTotals() {
        totalsArray = intArrayOf(0,0,0)
    }

    fun continentTotals(continentData: List<ContinentDataset>): IntArray {
        var cases = 0
        var recovered = 0
        var deaths = 0
        var activeCases = 0
        var critical = 0

        for (data in continentData) {
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

    fun continentCountryList(): HashMap<String, Array<String>> {
        val hashMap = HashMap<String, Array<String>>()
        for (data in AppConstants.CONTINENT_DATA) {
            hashMap[data.continent!!] = data.countriesOnContinent!!
        }
        return hashMap
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

    fun findDelay(currentTime: String): Long {
        val time = currentTime.split(":")[1].toLong()
        return (10L - (time % 10L)) * 60 * 1025
    }

    companion object {
        fun getInstance(): AppUtils {
            return AppUtils()
        }
    }
}