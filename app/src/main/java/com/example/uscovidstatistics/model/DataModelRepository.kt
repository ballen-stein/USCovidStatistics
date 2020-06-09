package com.example.uscovidstatistics.model

import com.example.uscovidstatistics.appconstants.AppConstants
import com.example.uscovidstatistics.model.apidata.*
import dagger.Component
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DataModelRepository {

    @Provides
    fun getDataModel(): DataModelRepository {
        return DataModelRepository()
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