package com.example.uscovidstatistics.manualdependency

import com.example.uscovidstatistics.model.DataModelRepository

class DependencyInjectorImpl : DependencyInjector {
    override fun covidDataRepository(): DataModelRepository {
        return DataModelRepository()
    }
}