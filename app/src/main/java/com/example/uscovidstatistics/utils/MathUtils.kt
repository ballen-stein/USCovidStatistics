package com.example.uscovidstatistics.utils

import com.example.uscovidstatistics.appconstants.AppConstants
import java.math.BigDecimal
import java.text.NumberFormat

class MathUtils {

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
}