package com.example.uscovidstatistics.model

import com.squareup.moshi.Json

class JhuCountryDataset {
    @Json(name = "country") var country: String? = null
    @Json(name = "province") var province: Array<String>? = null
    @Json(name = "timeline") var timeline: Timeline? = null

    class Timeline {
        @Json(name = "cases") var cases: Cases? = null
        //@Json(name = "deaths") var deaths: Array<Int>? = null
        //@Json(name = "recovered") var recovered: Array<Int>? = null

        class Cases {
            var caseNum: Array<Int>? = null
        }
    }
}