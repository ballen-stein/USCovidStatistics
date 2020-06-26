package com.example.uscovidstatistics.model.apidata

class NotificationDataset {
    var name: String = ""
    var casesValue: Int = 0
    var casesMetricMet: Boolean = false
    var deathValue: Int = 0
    var deathMetricMet: Boolean = false
    var recoverValue: Int = 0
    var recoverMetricMet: Boolean = false

    fun printData() {
        println("$name\n$casesValue\n$casesMetricMet\n$recoverValue\n$recoverMetricMet\n$deathValue\n$deathMetricMet")
    }
}