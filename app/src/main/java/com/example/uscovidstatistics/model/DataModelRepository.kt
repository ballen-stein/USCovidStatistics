package com.example.uscovidstatistics.model

import com.example.uscovidstatistics.appconstants.AppConstants
import com.example.uscovidstatistics.model.apidata.*
import dagger.Module
import dagger.Provides

@Module
class DataModelRepository {

    @Provides
    fun getDataModel(): DataModelRepository {
        return DataModelRepository()
    }

    fun getUsData(): List<StateDataset> {
        return AppConstants.Us_Data
    }

    fun getUsState(): StateDataset {
        return AppConstants.Us_State_Data
    }

    fun getUsMapped(): HashMap<String, StateDataset> {
        return AppConstants.Us_State_Data_Mapped
    }

    fun getContinentData(): List<ContinentDataset> {
        return AppConstants.Continent_Data
    }

    fun getCountryData(): JhuCountryDataset {
        return AppConstants.Country_Data
    }

    fun getProvinceData(): JhuProvinceDataset {
        return AppConstants.Country_Province_Data
    }
}