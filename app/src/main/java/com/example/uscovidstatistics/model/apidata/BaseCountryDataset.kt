package com.example.uscovidstatistics.model.apidata

import com.squareup.moshi.Json

class BaseCountryDataset {
    @Json(name = "updated") var timeUpdated: String? = null
    @Json(name = "country") var country: String? = null
    @Json(name = "countryInfo") var countryInfo: CountryInfo? = null
    @Json(name = "cases") var cases: Int? = null
    @Json(name = "todayCases") var todayCases: Int? = null
    @Json(name = "deaths") var deaths: Int? = null
    @Json(name = "todayDeaths") var todayDeaths: Int? = null
    @Json(name = "recovered") var recovered: Int? = null
    @Json(name = "active") var activeCases: Int? = null
    @Json(name = "critical") var criticalCases: Int? = null
    @Json(name = "casesPerOneMillion") var perMillionCases: Double? = null
    @Json(name = "deathsPerOneMillion") var perMillionDeaths: Double? = null
    @Json(name = "tests") var tests: Int? = null
    @Json(name = "testsPerOneMillion") var perMillionTests: Double? = null
    @Json(name = "population") var population: Int? = null
    @Json(name = "continent") var continent: String? = null
    @Json(name = "activePerOneMillion") var perMillionActive: Double? = null
    @Json(name = "recoveredPerOneMillion") var perMillionRecovered: Double? = null
    @Json(name = "criticalPerOneMillion") var perMillionCritical: Double? = null

    class CountryInfo {
        @Json(name = "_id") var _id: Int? = null
        @Json(name = "iso2") var iso2: String? = null
        @Json(name = "iso3") var iso3: String? = null
        @Json(name = "lat") var lat: Double? = null
        @Json(name = "long") var long: Double? = null
        @Json(name = "flag") var countryFlag: String? = null
    }
}