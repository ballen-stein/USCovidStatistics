package com.example.uscovidstatistics.model

import com.squareup.moshi.Json

class JhuCountryDataSet {
    @Json(name = "country") var country: String? = null
    @Json(name = "stats") var stats: Stats? = null
    @Json(name = "coordinates") var coordinates: Coordinates? = null

    class Stats {
        @Json(name = "confirmed") var confirmed: Int? = null
        @Json(name = "deaths") var deaths: Int? = null
        @Json(name = "recovered") var recovered: Int? = null
    }

    class Coordinates {
        @Json(name = "latitude") var latitude: String? = null
        @Json(name = "longitude") var longitude: String? = null
    }

}