package com.example.uscovidstatistics.manualdependency

import com.example.uscovidstatistics.model.DataModelRepository

interface DependencyInjector {
    fun covidDataRepository() : DataModelRepository
}