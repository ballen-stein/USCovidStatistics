package com.example.uscovidstatistics.model

import com.squareup.moshi.Json

class ContinentDataset {
    @Json(name = "updated") var timeUpdated: String? = null
    @Json(name = "cases") var cases: Int? = null
    @Json(name = "todayCases") var todayCases: Int? = null
    @Json(name = "deaths") var deaths: Int? = null
    @Json(name = "todayDeaths") var todayDeaths: Int? = null
    @Json(name = "recovered") var recovered: String? = null
    @Json(name = "active") var activeCases: Int? = null
    @Json(name = "critical") var criticalCases: Int? = null
    @Json(name = "casesPerOneMillion") var perMillionCases: Double? = null
    @Json(name = "deathsPerOneMillion") var perMillionDeaths: Double? = null
    @Json(name = "tests") var tests: Int? = null
    @Json(name = "testsPerOneMillion") var perMillionTests: Double? = null
    @Json(name = "population") var population: String? = null
    @Json(name = "continent") var continent: String? = null
    @Json(name = "activePerOneMillion") var perMillionActive: Double? = null
    @Json(name = "recoveredPerOneMillion") var perMillionRecovered: Double? = null
    @Json(name = "criticalPerOneMillion") var perMillionCritical: Double? = null
    @Json(name = "countries") var countriesOnContinent: Array<String>? = null
}