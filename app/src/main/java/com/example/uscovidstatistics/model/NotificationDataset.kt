package com.example.uscovidstatistics.model

import android.util.Log

class NotificationDataset {
    var name: String = ""
    var casesValue: Int = 0
    var casesMetricMet: Boolean = false
    var deathValue: Int = 0
    var deathMetricMet: Boolean = false
    var recoverValue: Int = 0
    var recoverMetricMet: Boolean = false
    var setData: String = ""

    fun createSetData() {
        setData ="$name/$casesValue/$casesMetricMet/$recoverValue/$recoverMetricMet/$deathValue/$deathMetricMet"
    }

    fun printData() {
        Log.d("CovidTesting", "Data as set : $name/$casesValue/$casesMetricMet/$recoverValue/$recoverMetricMet/$deathValue/$deathMetricMet")
    }
}