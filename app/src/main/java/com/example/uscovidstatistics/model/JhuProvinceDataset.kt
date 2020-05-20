package com.example.uscovidstatistics.model

import com.squareup.moshi.Json

class JhuProvinceDataset {
    @Json(name = "country") var country: String? = null
    @Json(name = "province") var province: String? = null
    @Json(name = "timeline") var timeline: Timeline? = null

    class Timeline {
        @Json(name = "cases") var cases: List<Int>? = null
        @Json(name = "deaths") var deaths: Array<Int>? = null
        @Json(name = "recovered") var recovered: Array<Int>? = null
    }
}