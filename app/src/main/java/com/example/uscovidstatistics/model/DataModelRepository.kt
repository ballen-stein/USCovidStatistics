package com.example.uscovidstatistics.model

import com.example.uscovidstatistics.appconstants.AppConstants
import com.example.uscovidstatistics.model.apidata.*

class DataModelRepository {
    fun getData(dataSpecifier: Int): Any {
        return when (dataSpecifier) {
            0 -> AppConstants.WORLD_DATA
            1 -> AppConstants.US_DATA
            2 -> AppConstants.US_STATE_DATA
            3 -> AppConstants.CONTINENT_DATA
            4 -> AppConstants.COUNTRY_DATA
            5 ->  AppConstants.COUNTRY_PROVINCE_DATA
            else -> AppConstants.WORLD_DATA
        }
    }

    fun getWorldData(): List<BaseCountryDataset> {
        return AppConstants.WORLD_DATA
    }

    fun getUsData(): List<StateDataset> {
        return AppConstants.US_DATA
    }

    fun getUsState(): StateDataset {
        return AppConstants.US_STATE_DATA
    }

    fun getUsMapped(): HashMap<String, StateDataset> {
        return AppConstants.US_STATE_DATA_MAPPED
    }

    fun getContinentData(): List<ContinentDataset> {
        return AppConstants.CONTINENT_DATA
    }

    fun getCountryData(): JhuCountryDataset {
        return AppConstants.COUNTRY_DATA
    }

    fun getProvinceData(): JhuProvinceDataset {
        return AppConstants.COUNTRY_PROVINCE_DATA
    }
}