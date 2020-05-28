package com.example.uscovidstatistics.model.apidata

import com.squareup.moshi.Json

class JhuProvinceDataset {
    @Json(name = "country") var country: String? = null
    @Json(name = "province") var province: String? = null
    @Json(name = "timeline") var timeline: Timeline? = null

    class Timeline {
        @Json(name = "cases") var cases: Map<String, Int>? = null
        @Json(name = "deaths") var deaths: Map<String, Int>? = null
        @Json(name = "recovered") var recovered: Map<String, Int>? = null
    }
}