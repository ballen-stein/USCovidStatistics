package com.example.uscovidstatistics.model

import com.squareup.moshi.Json

class StateDataset {
    @Json(name = "state") var state: String? = null
    @Json(name = "updated") var timeUpdated: String? = null
    @Json(name = "cases") var cases: Int? = null
    @Json(name = "todayCases") var todayCases: Int? = null
    @Json(name = "deaths") var deaths: Int? = null
    @Json(name = "todayDeaths") var todayDeaths: Int? = null
    @Json(name = "active") var activeCases: Int? = null
    @Json(name = "casesPerOneMillion") var perMillionCases: Double? = null
    @Json(name = "deathsPerOneMillion") var perMillionDeaths: Double? = null
    @Json(name = "tests") var tests: Int? = null
    @Json(name = "testsPerOneMillion") var perMillionTests: Double? = null
}